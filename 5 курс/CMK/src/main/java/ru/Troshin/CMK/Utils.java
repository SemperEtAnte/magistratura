package ru.Troshin.CMK;

import java.util.Arrays;

public final class Utils
{
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
         return -1;
      else
         return x[0] += b;
   }

   public static int getNOD(int a, int b)
   {
      while (a != b)
         if (a > b)
            a -= b;
         else
            b -= a;
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
