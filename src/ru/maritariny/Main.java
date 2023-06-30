package ru.maritariny;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        //BufferedReader reader = new BufferedReader(new FileReader("test.txt"));
        String s = reader.readLine();
        int n = Integer.parseInt(reader.readLine());
        List<long[]> list = new ArrayList<>();
        char[] ss = s.toCharArray();
        int i = 0;
        long sum = 0; // нарастающим итогом длина исходной строки
        long sumRle = 0; // нарастающим итогом длина сжатой строки
        while (i < ss.length) {
            long beginI = i;
            char ch = ss[i];
            while (!Character.isLetter(ch)) {
                i++;
                ch = ss[i];
            }
            long newSumRle = i - beginI + 1;
            long newSum = 0;
            if (i - beginI <= 0) {
                newSum = 1;
            } else {
                newSum = Long.parseLong(s.substring((int)beginI, i));
            }
            sum += newSum;
            sumRle += newSumRle;
            list.add(new long[]{sum, sumRle});
            i++;
        }

        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < n; j++) {
            String[] parts = reader.readLine().split(" ");
            long left = Long.parseLong(parts[0]);
            long right = Long.parseLong(parts[1]);
            if (right - left <= 1) {
                sb.append(right - left + 1);
                sb.append("\n");
                continue;
            }
            int indexLeft = binarySearch(list, left);
            int indexRight = binarySearch(list, right);
            long res = 0;
            if (indexLeft == indexRight) {
                if ((right - left) <= 1) {
                    res = right - left + 1;
                } else {
                    long d = right - left + 1;
                    res = (long)Math.ceil(Math.log10(d + 0.1)) + 1;
                }
            } else {
                long d = list.get(indexLeft)[0] - left + 1;
                if (d == 1) {
                    res = 1;
                } else {
                    res = (long)Math.ceil(Math.log10(d + 0.1)) + 1;
                }
                d = right - list.get(indexRight - 1)[0];
                if (d == 1) {
                    res++;
                } else {
                    res += (long)Math.ceil(Math.log10(d + 0.1)) + 1;
                }
                if (indexRight - indexLeft > 1) {
                    res += list.get(indexRight - 1)[1] - list.get(indexLeft)[1];
                }
            }
            sb.append(res);
            sb.append("\n");
        }

        System.out.println(sb);
        reader.close();
    }
    private static int binarySearch(List<long[]> list, long num) {
        if (num <= list.get(0)[0]) {
            return 0;
        }
        int length = list.size();
        if (num == list.get(length - 1)[0]) {
            return length - 1;
        }
        int begin = 0;
        int end = length - 1;
        while (begin + 1 < end) {
            int mid = (begin + end) / 2;
            if (list.get(mid)[0] == num) {
                return mid;
            } else if (list.get(mid)[0] >num) {
                end = mid;
            } else {
                begin = mid;
            }
        }
        return begin + 1;
    }
}