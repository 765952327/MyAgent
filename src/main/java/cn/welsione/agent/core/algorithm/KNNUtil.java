package cn.welsione.agent.core.algorithm;

import com.huaban.analysis.jieba.JiebaSegmenter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class KNNUtil {
    private static final JiebaSegmenter js = new JiebaSegmenter();
    private static final List<String> storedStrings = new ArrayList<>();
    private static final List<double[]> storedVectors = new ArrayList<>();
    private static final Set<String> vocabulary = new HashSet<>();
    
    public List<String> get(String input){
        if (vocabulary.isEmpty() || storedStrings.isEmpty()){
            return new ArrayList<>();
        }
        // 分词和向量化输入字符串
        String[] tokenizedInput = tokenize(input);
        double[] inputVector = vectorize(tokenizedInput, vocabulary);
        // 使用kNN算法查询相似字符串
        int k = 3; // 设置k的值
        List<Integer> nearestIndex = classify(storedVectors, inputVector, k);
        
        
        double similarityThreshold = 0.3;
        List<String> result = new ArrayList<>();
        // 计算相似度
        for (int i = 0; i < nearestIndex.size(); i++) {
            double similarity = calculateCosineSimilarity(storedVectors.get(i), inputVector);
            if (similarity >= similarityThreshold) {
                result.add(storedStrings.get(i));
            }
        }
        // 如果相似度高于阈值，则返回匹配结果，否则保存并返回当前字符串
        return result;
    }
    
    public void put(String input) {
        // 分词和向量化输入字符串
        String[] tokenizedInput = tokenize(input);
        if (vocabulary.isEmpty()) {
            // 更新词汇表
            updateVocabulary(tokenizedInput);
        }
        double[] inputVector = vectorize(tokenizedInput, vocabulary);
        storedStrings.add(input);
        storedVectors.add(inputVector);
    }
    
    public List<String> processString(String input) {
        // 分词和向量化输入字符串
        String[] tokenizedInput = tokenize(input);
        if (vocabulary.isEmpty()) {
            // 更新词汇表
            updateVocabulary(tokenizedInput);
        }
        double[] inputVector = vectorize(tokenizedInput, vocabulary);
        
        // 如果存储的字符串为空，则直接保存并返回
        if (storedStrings.isEmpty()) {
            storedStrings.add(input);
            storedVectors.add(inputVector);
            return null;
        }
        
        // 使用kNN算法查询相似字符串
        int k = 3; // 设置k的值
        List<Integer> nearestIndex = classify(storedVectors, inputVector, k);
        
        
        double similarityThreshold = 0.8;
        List<String> result = new ArrayList<>();
        // 计算相似度
        for (int i = 0; i < nearestIndex.size(); i++) {
            double similarity = calculateCosineSimilarity(storedVectors.get(i), inputVector);
            if (similarity >= similarityThreshold) {
                result.add(storedStrings.get(i));
            } else {
                storedStrings.add(input);
                storedVectors.add(inputVector);
            }
        }
        // 如果相似度高于阈值，则返回匹配结果，否则保存并返回当前字符串
        return result;
    }
    
    // 分词函数
    private String[] tokenize(String text) {
        List<String> process = js.sentenceProcess(text);
        return process.toArray(new String[0]);
    }
    
    // 向量化函数
    private double[] vectorize(String[] tokens, Set<String> vocabulary) {
        double[] vector = new double[vocabulary.size()];
        Map<String, Integer> tokenCount = new HashMap<>();
        for (String token : tokens) {
            tokenCount.put(token, tokenCount.getOrDefault(token, 0) + 1);
        }
        int index = 0;
        for (String word : vocabulary) {
            vector[index++] = tokenCount.getOrDefault(word, 0);
        }
        return vector;
    }
    
    // 更新词汇表函数
    private void updateVocabulary(String[] tokens) {
        vocabulary.addAll(Arrays.asList(tokens));
    }
    
    // kNN分类函数
    private List<Integer> classify(List<double[]> storedVectors, double[] inputVector, int k) {
        if (storedVectors.isEmpty()) {
            throw new IllegalArgumentException("storedVectors is empty");
        }
        
        int n = storedVectors.size();
        double[][] distances = new double[n][2];
        
        // 计算所有存储向量与输入向量之间的欧几里得距离
        for (int i = 0; i < n; i++) {
            double dist = 0.0;
            for (int j = 0; j < inputVector.length; j++) {
                dist += Math.pow(storedVectors.get(i)[j] - inputVector[j], 2);
            }
            distances[i][0] = Math.sqrt(dist); // 距离
            distances[i][1] = i;               // 向量索引
        }
        
        // 按距离排序
        Arrays.sort(distances, Comparator.comparingDouble(a -> a[0]));
        
        // 返回距离最小的k个向量索引
        List<Integer> nearestIndices = new ArrayList<>();
        double minDistance = distances[0][0];
        for (int i = 0; i < n && nearestIndices.size() < k; i++) {
            if (distances[i][0] == minDistance || nearestIndices.size() < k) {
                nearestIndices.add((int) distances[i][1]);
            } else {
                break;
            }
        }
        
        return nearestIndices;
    }
    
    
    // 计算余弦相似度
    private double calculateCosineSimilarity(double[] vectorA, double[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}