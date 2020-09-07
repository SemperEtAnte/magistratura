package ru.Troshin.CMK;

import java.math.BigInteger;
import java.util.Arrays;

public final class Utils
{

    //Возведение в степень бинарным методом
    public static BigInteger binaryPow(long a, long n, long mod)
    {
        char[] digits = Long.toBinaryString(n).toCharArray();
        BigInteger z = BigInteger.ONE;
        BigInteger ap = BigInteger.valueOf(a);
        for (char c : digits)
        {
            z = z.multiply(z);
            if (c == '1')
            {
                z = z.multiply(ap);
            }
        }
        return z.mod(BigInteger.valueOf(mod));
    }

    public static boolean isPseudoSimple(long n, int x)
    {
        long s = (long) (Math.log(n) / Math.log(2));
        long t;
        while (true)
        {
            long pow = 1L << s;
            double temp = (n - 1D) / pow;
            if (((long) temp == temp && temp % 2 == 1))
            {
                t = (long) temp;
                break;
            }
            --s;
        }
        if (Utils.getNOD(x, n) != 1)
        {
            return false;
        }
        long y0 = Utils.binaryPow(x, t, n).longValue();
        if (Math.abs(y0 % n) == 1)
        {
            return true;
        }
        long yj = y0;

        for (long j = 1; j < s; ++j)
        {
            long yj1 = yj;
            yj = (yj1 * yj1) % n;
            long mod = yj % n;
            System.out.println("При x = " + x + " и j = " + j + " Yj = " + yj + " И " + yj + "mod" + n + " = " + mod);
            if (Math.abs(mod) == 1)
            {
                break;
            }
        }
        return yj % n != 1;

    }

    private static int ExtendedEuclid(int a, int b, int[] x, int[] y)
    {
        if (a == 0)
        {
            x[0] = 0;
            y[0] = 1;
            return b;
        }
        int x1[] = {0}, y1[] = {0};
        int d = ExtendedEuclid(b % a, a, x1, y1);
        x[0] = y1[0] - (b / a) * x1[0];
        y[0] = x1[0];
        return d;

    }

    public static int getReversedElement(int a, int b)
    {
        int x[] = new int[1];
        int y[] = new int[1];
        int d = ExtendedEuclid(a, b, x, y);
        if (d != 1)
        {
            return -1;
        }
        else
        {
            return x[0] += b;
        }
    }

    public static long getNOD(long a, long b)
    {
        while (a != b)
        {
            if (a > b)
            {
                a -= b;
            }
            else
            {
                b -= a;
            }
        }
        return a;
    }

    public static double[][] copyOfMatrix(double[][] a)
    {
        if (a.length == 0)
        {
            return new double[0][0];
        }
        double res[][] = new double[a.length][a[0].length];
        for (int i = 0; i < a.length; ++i)
        {
            res[i] = Arrays.copyOf(a[i], a[i].length);
        }
        return res;
    }
}
