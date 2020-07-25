package net.pupil.newlife;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testIsMatch() {
        System.out.println(isMatch("abcd", "d*"));
    }

    public boolean isMatchComplicated(String s, String p) {
        int sLen = s.length(), pLen = p.length();
        boolean[][] memory = new boolean[sLen+1][pLen+1];
        memory[0][0] = true;
        for(int i = 0; i <= sLen; i++) {
            for(int j = 1; j <= pLen; j++) {
                if(p.charAt(j-1) == '*') {
                    memory[i][j] = memory[i][j-2] || (i > 0 && (s.charAt(i-1) == p.charAt(j-2) ||
                            p.charAt(j-2) == '.') && memory[i-1][j]);
                }else {
                    memory[i][j] = i > 0 && (s.charAt(i-1) == p.charAt(j-1) || p.charAt(j-1) == '.')
                            && memory[i-1][j-1];
                }
            }
        }
        return memory[sLen][pLen];
    }

    /**
     * 和上面的方法（isMatchComplicated）相比逻辑差不多，只是减少了空间复杂度，把用来存放s匹配结果的数组
     * 由s的长度+1缩短为2，只保留了这一次与p的匹配情况和上一次与p的匹配情况，因为再早的已经用不到了
     */
    public boolean isMatch(String s, String p) {
        int sLen = s.length(), pLen = p.length();
        boolean[][] memory = new boolean[2][pLen+1]; //外层数组的长度变为2
        memory[0][0] = true;
        int cur = 0, pre = 0;
        for(int i = 0; i <= sLen; i++) {
            cur = i % 2; //巧妙的用取余的方式来标记这一次和上一次
            pre = (i + 1) % 2;
            //到s的第3个字符时，清空第1个字符与p的匹配情况，第4个字符时，清空第2个，以此类推
            if(i > 1) {
                for(int j = 0; j <= pLen; j++) {
                    memory[cur][j] = false; //置为false，就是清空
                }
            }
            //以下逻辑就类似于上面的原始方法了（isMatchComplicated）
            for(int j = 1; j <= pLen; j++) {
                if(p.charAt(j-1) == '*') {

                    memory[cur][j] = memory[cur][j-2] || (i > 0 && (s.charAt(i-1) == p.charAt(j-2) ||
                            p.charAt(j-2) == '.') && memory[pre][j]);
                }else {
                    memory[cur][j] = i > 0 && (s.charAt(i-1) == p.charAt(j-1) || p.charAt(j-1) == '.')
                            && memory[pre][j-1];
                }
            }
        }
        return memory[cur][pLen];
    }
}