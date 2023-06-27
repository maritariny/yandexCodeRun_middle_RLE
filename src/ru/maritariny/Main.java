package ru.maritariny;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        //BufferedReader reader = new BufferedReader(new FileReader("test.txt"));

        String s = reader.readLine();
        int n = Integer.parseInt(reader.readLine());
        List<Long> list = RLE(s);
        Map<Point, Long> map = new HashMap<>(n);
        List<Point> listPoint = new ArrayList<>(n);
        int length = list.size();
        for (int i = 0; i < n; i++) {
            String[] parts = reader.readLine().split(" ");
            long left = Long.parseLong(parts[0]);
            long right = Long.parseLong(parts[1]);
            listPoint.add(new Point(left, right));
        }
        List<Point> sortedList = new ArrayList<>(listPoint);
        Collections.sort(sortedList);
        int first = 0;
        for (Point point : sortedList) {
            long left = point.getLeft();
            long right = point.getRight();
            int begin = first; // номер начала интервала в списке list. Начала интервалов упорядочены
            long cur = list.get(first);
            if (cur != left) {
                for (int i = (first + 1); i < length; i++) {
                    if (list.get(i) == left) {
                        begin = i;
                        first = i;
                        break;
                    } else if (list.get(i) > left) {
                        begin = i - 1;
                        first = i - 1;
                        break;
                    }
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
            map.put(point, result);
            //System.out.println(result);
        }

        for (Point point : listPoint) {
            System.out.println(map.get(point));
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
class Point implements Comparable<Point>{
    private long left;
    private long right;

    public Point(long left, long right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return left == point.left && right == point.right;
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }

    @Override
    public int compareTo(Point o) {
        if (this.left == o.getLeft()) {
            return Long.compare(this.right, o.getRight());
            //return o.getLeft() - this.left;
        }
        return Long.compare(this.left, o.getLeft());
        //return o.getW() - this.right;
    }

    public long getLeft() {
        return left;
    }

    public long getRight() {
        return right;
    }
}