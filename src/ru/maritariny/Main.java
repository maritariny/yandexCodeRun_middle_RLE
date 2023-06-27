package ru.maritariny;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        //BufferedReader reader = new BufferedReader(new FileReader("test.txt"));

        String s = reader.readLine();
        int n = Integer.parseInt(reader.readLine());
        List<Long> list = RLE(s);
        int length = list.size();
        //System.out.println(list);
        for (int i = 0; i < n; i++) {
            String[] parts = reader.readLine().split(" ");
            long left = Long.parseLong(parts[0]);
            long right = Long.parseLong(parts[1]);
            int begin = 0;
            for (int j = 0; j < length; j++) {
                if (list.get(j) == left) {
                    begin = j;
                    break;
                }  else if (list.get(j) > left) {
                    begin = j - 1;
                    break;
                }
            }
            long result = 0L;
            long next;

            boolean end = false;
            while (left <= right && !end) {
                if ((begin + 1) <= (list.size() - 1)) {
                    if (right < list.get(begin + 1)) {
                       next = right + 1;
                       end = true;
                    } else {
                       next =  list.get(begin + 1);
                    }
                } else {
                    next = right + 1;
                }
                long d = next - left;
                if (d == 0) {
                    result++;
                } else if (d <= 2) {
                    result += d;
                } else {
                    result = result + (long)Math.ceil(Math.log10(d + 0.1)) + 1;
                }
                left = next;
                begin++;
            }
            System.out.println(result);
        }
        reader.close();
    }

    private static List<Long> RLE(String s) {
        List<Long> list = new ArrayList<>();
        char[] ss = s.toCharArray();
        boolean lastIsLetter = false;
        StringBuilder curNumber = new StringBuilder();
        Long it = 1L;
        if (ss.length == 1) {
            list.add(it);
            return list;
        } else if (ss.length == 2 && Character.isLetter(ss[0])) {
            list.add(1L);
            list.add(2L);
            return list;
        }
        boolean firstIter = true;
        boolean lastLetterIsOK = true;
        for (char ch : ss) {
            boolean isLetter = Character.isLetter(ch);
            if (isLetter) {
                if (firstIter) {
                    firstIter = false;
                    lastIsLetter = isLetter;
                    continue;
                }
                if (curNumber.length() != 0) {
                    long number = Long.parseLong(curNumber.toString());
                    list.add(it); // начало цифр
                    it = it + number;
                    lastLetterIsOK = false;
                } else {
                    if (lastIsLetter && (lastLetterIsOK)) {
                        list.add(it); // буква сразу после цифр
                        it++;
                    } else {
                        lastLetterIsOK = true;
                    }
                }
                curNumber.setLength(0);
            } else {
                firstIter = false;
                curNumber.append(ch);
                if (lastIsLetter && lastLetterIsOK) {
                    list.add(it); // символ перед цифрой
                    it++;
                }
            }
            lastIsLetter = isLetter;
        }
        if (lastIsLetter && lastLetterIsOK) {
            list.add(it); // последний символ буква, которая не после цифры
        }
        //list.add(it - 1);

        return list;
    }

}
