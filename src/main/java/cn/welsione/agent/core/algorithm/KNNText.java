package cn.welsione.agent.core.algorithm;

import java.util.*;
import java.util.stream.Collectors;

public class KNNText {
    public static void main(String[] args) {
        String[] documents = {
                "今天天气真好",
                "今天下雨了",
                "天气不错",
                "下雨天真讨厌",
                "今天心情很好"
        };
        String query = "天气真好";
        
        // 文本预处理和向量化
        List<String[]> tokenizedDocuments = Arrays.stream(documents)
                .map(KNNText::tokenize)
                .collect(Collectors.toList());
        String[] tokenizedQuery = tokenize(query);
        System.out.println("向量: " + Arrays.toString(tokenizedQuery));
        
        // 构建词汇表
        Set<String> vocabulary = tokenizedDocuments.stream()
                .flatMap(Arrays::stream)
                .collect(Collectors.toSet());
        System.out.println("建词汇表: ");
        for (String word : vocabulary) {
            System.out.println(word);
        }
        
        // 将文档和查询转换为向量
        double[][] documentVectors = tokenizedDocuments.stream()
                .map(tokens -> vectorize(tokens, vocabulary))
                .toArray(double[][]::new);
        System.out.println("向量: " + Arrays.deepToString(documentVectors));
        double[] queryVector = vectorize(tokenizedQuery, vocabulary);
        System.out.println("查询向量: " + Arrays.toString(queryVector));
        
        // 输入k的值
        Scanner scanner = new Scanner(System.in);
        System.out.print("输入k的值: ");
        int k = scanner.nextInt();
        scanner.close();
        
        // 进行kNN分类
        int predictedIndex = classify(documentVectors, queryVector, k);
        System.out.println("预测的文档索引: " + predictedIndex);
        System.out.println("预测的文档内容: " + documents[predictedIndex]);
    }
    
    // 分词函数（简单示例）
    public static String[] tokenize(String text) {
        return text.split("");
    }
    
    // 向量化函数
    public static double[] vectorize(String[] tokens, Set<String> vocabulary) {
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
    
    // kNN分类函数
    public static int classify(double[][] documentVectors, double[] queryVector, int k) {
        int n = documentVectors.length;
        double[][] distances = new double[n][2];
        
        // 计算所有文档向量与查询向量之间的欧几里得距离
        for (int i = 0; i < n; i++) {
            double dist = 0.0;
            for (int j = 0; j < queryVector.length; j++) {
                dist += Math.pow(documentVectors[i][j] - queryVector[j], 2);
            }
            distances[i][0] = Math.sqrt(dist); // 距离
            distances[i][1] = i;               // 文档索引
        }
        
        // 按距离排序
        Arrays.sort(distances, Comparator.comparingDouble(a -> a[0]));
        
        // 选择距离最小的k个文档
        Map<Integer, Integer> votes = new HashMap<>();
        for (int i = 0; i < k; i++) {
            int index = (int) distances[i][1];
            votes.put(index, votes.getOrDefault(index, 0) + 1);
        }
        
        // 找到得票最多的文档索引
        return Collections.max(votes.entrySet(), Map.Entry.comparingByValue()).getKey();
    }
}