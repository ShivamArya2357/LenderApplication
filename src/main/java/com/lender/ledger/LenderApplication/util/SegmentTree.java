package com.lender.ledger.LenderApplication.util;

import org.springframework.stereotype.Component;

@Component
public class SegmentTree {

    private int[] segmentTree;

    public void initialize(int[] arr, int n) {

        int height = (int) (Math.ceil(Math.log(n) / Math.log(2)));
        int maxSize = 2 * (int) Math.pow(2, height) - 1;
        segmentTree = new int[maxSize];
        constructSTUtil(arr, 0, n - 1, 0);
    }

    private int getMid(int start, int end) {

        return start + (end - start) / 2;
    }

    private int constructSTUtil(int[] arr, int start, int end, int sIndex) {

        if (start == end) {
            segmentTree[sIndex] = arr[start];
            return arr[start];
        }
        int mid = getMid(start, end);
        segmentTree[sIndex] = constructSTUtil(arr, start, mid, 2 * sIndex + 1)
                + constructSTUtil(arr, mid + 1, end, 2 * sIndex + 2);
        return segmentTree[sIndex];
    }

    public void update(int[] arr, int n, int aIndex, int newVal) {

        if (aIndex < 0 || aIndex > n - 1) {
            System.out.println("Invalid input");
            return;
        }
        int diff = newVal - arr[aIndex];
        arr[aIndex] = newVal;
        updateUtil(0, n - 1, aIndex, diff, 0);
    }

    private void updateUtil(int start, int end, int aIndex, int diff, int si) {

        if (aIndex < start || aIndex > end) {
            return;
        }
        segmentTree[si] = segmentTree[si] + diff;
        if (start != end) {
            int mid = getMid(start, end);
            updateUtil(start, mid, aIndex, diff, 2 * si + 1);
            updateUtil(mid + 1, end, aIndex, diff, 2 * si + 2);
        }
    }

    public int getSum(int n, int start, int end) {

        if (start < 0 || end > n - 1 || start > end) {
            System.out.println("Invalid input");
            return -1;
        }
        return getSumUtil(0, n - 1, start, end, 0);
    }

    private int getSumUtil(int start, int end, int qStart, int qEnd, int si) {

        if (qStart <= start && qEnd >= end) {
            return segmentTree[si];
        }
        if (end < qStart || start > qEnd) {
            return 0;
        }
        int mid = getMid(start, end);
        return getSumUtil(start, mid, qStart, qEnd, 2 * si + 1)
                + getSumUtil(mid + 1, end, qStart, qEnd, 2 * si + 2);
    }

    public int getCeilOfANumber(int si, int start, int end, int l, int r, int key) {

        if (start > r || end < l) {
            return -1;
        }
        if (l <= start && end <= r) {
            if (segmentTree[si] <= key) {
                return -1;
            }
            while (start != end) {
                int mid = start + (end - start) / 2;
                if (segmentTree[2 * si] > key) {
                    si = 2 * si;
                    end = mid;
                } else {
                    si = 2 * si + 1;
                    start = mid + 1;
                }
            }
            return start;
        }
        int mid = start + (end-start) / 2;
        int rs = getCeilOfANumber(2 * si, start, mid, l, r, key);
        if(rs != -1) return rs;
        return getCeilOfANumber(2 * si + 1, mid+1, end, l ,r, key);
    }
}
