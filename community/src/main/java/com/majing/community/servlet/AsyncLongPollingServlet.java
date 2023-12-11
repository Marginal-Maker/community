package com.majing.community.servlet;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.AsyncEvent;
import jakarta.servlet.AsyncListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author majing
 * @date 2023-11-27 20:14
 * @Description
 */
@WebServlet(urlPatterns = {"/tee"}, asyncSupported = true)
public class AsyncLongPollingServlet extends HttpServlet {
    public static String message = "welcome";
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException {
        //建立AsyncContext
        AsyncContext asyncContext = request.startAsync();
        long sleepTime = 4000;
        asyncContext.addListener(new AsyncListener() {
            @Override
            public void onComplete(AsyncEvent asyncEvent) throws IOException {
                System.out.println("AsyncLongPollingServlet.onComplete");
            }

            @Override
            public void onTimeout(AsyncEvent asyncEvent) throws IOException {
                System.out.println("AsyncLongPollingServlet.onTimeout");
            }

            @Override
            public void onError(AsyncEvent asyncEvent) throws IOException {
                System.out.println("AsyncLongPollingServlet.onError");

            }

            @Override
            public void onStartAsync(AsyncEvent asyncEvent) throws IOException {
                // 不会看到有相关的打印信息，根据注解
                // Notifies this AsyncListener that a new asynchronous cycle is being initiated via
                // a call to one of the ServletRequest.startAsync methods
                // 这是在前面调用的，已经是历史了
                System.out.println("AsyncLongPollingServlet.onStartAsync");
            }
        });
        //【5】启动异步线程进行处理
        asyncContext.start(new Runnable() {
            @Override
            public void run() {
                //【5.1】hold住连接直到超时了
                long curSleepTime = 0;
                boolean timeout = false;
                while(message == null){
                    curSleepTime +=  200;
                    try {
                        TimeUnit.MILLISECONDS.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(curSleepTime>=(sleepTime)){
                        timeout=true;
                        break;
                    }
                }
                if(timeout){//超时了，直接返回即可.
                    asyncContext.complete();
                    return;
                }

                //【5.2】获取ServletResponse，返回信息
                PrintWriter pw = null;
                try {

                    pw = asyncContext.getResponse().getWriter();

                    pw.write(Objects.requireNonNullElse(message, ""));
                    pw.flush();
                    // 【5.3】必须明确通知AsyncContext已经完成
                    asyncContext.complete();
                    message = null;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
