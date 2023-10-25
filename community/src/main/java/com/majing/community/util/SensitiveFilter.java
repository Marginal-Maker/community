package com.majing.community.util;

import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author majing
 * @date 2023-10-23 13:53
 * @Description 敏感词过滤器
 */
@Component
public class SensitiveFilter {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);
    private static final String REPLACEMENT = "****";
    private final TrieNode rootNode = new TrieNode(false, new HashMap<>());

    @PostConstruct
    private void init() {
        try (
                InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("sensitive-words");
                BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))) {
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                this.updateTrie(keyword);
            }
        } catch (IOException e) {
            logger.error("加载敏感词文件失败" + e.getMessage());
        }
    }

    private void updateTrie(String keyword) {
        TrieNode tempNode = rootNode;
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            TrieNode sunNode = tempNode.getSunNodes().get(c);
            if (sunNode == null) {
                //初始化子节点
                sunNode = new TrieNode(false, new HashMap<>());
                tempNode.getSunNodes().put(c, sunNode);
            }
            tempNode = sunNode;
            if (i == keyword.length() - 1) {
                tempNode.setKeyWordEnd(true);
            }
        }
    }

    private Boolean isSymbol(Character c) {
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return "";
        }
        TrieNode currentNode = rootNode;
        int left = 0;
        int right = 0;
        StringBuilder stringBuilder = new StringBuilder();
        while (left < text.length()) {
            if (right < text.length()) {
                char currentChar = text.charAt(right);
                //跳过符号
                if (isSymbol(currentChar)) {
                    //如果￥在开头，比如￥开票，则转换成￥****
                    if (currentNode == rootNode) {
                        stringBuilder.append(currentChar);
                        left++;
                    }
                    //如果不是开头，如开￥票，则一并变成****
                    right++;
                    continue;
                }
                //检查下一级字符
                currentNode = currentNode.getSunNodes().get(currentChar);
                //子节点中不存在该敏感词
                if (currentNode == null) {
                    stringBuilder.append(text.charAt(left));
                    right = ++left;
                    currentNode = rootNode;
                }
                //子节点存在敏感词并且已经是叶子结点
                else if (currentNode.isKeyWordEnd()) {
                    stringBuilder.append(REPLACEMENT);
                    left = ++right;
                    currentNode = rootNode;
                }
                //是敏感词但是还没有到叶子节点
                else {
                    right++;
                }
            } else {
                stringBuilder.append(text.charAt(left));
                right = ++left;
                currentNode = rootNode;
            }
        }

        return stringBuilder.toString();
    }

    private static class TrieNode {
        //关键词结束的表示
        private boolean isKeyWordEnd;
        //当前节点的子节点
        private Map<Character, TrieNode> sunNodes;

        public TrieNode(boolean isKeyWordEnd, Map<Character, TrieNode> sunNodes) {
            this.isKeyWordEnd = isKeyWordEnd;
            this.sunNodes = sunNodes;
        }

        public boolean isKeyWordEnd() {
            return isKeyWordEnd;
        }

        public void setKeyWordEnd(boolean keyWordEnd) {
            isKeyWordEnd = keyWordEnd;
        }

        public Map<Character, TrieNode> getSunNodes() {
            return sunNodes;
        }

    }
}
