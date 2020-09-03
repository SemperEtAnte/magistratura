package ru.Troshin.CMK;

import ru.Troshin.CMK.Exceptions.IllegalKeyException;

import java.util.Arrays;

public class Matrix
{
   double[][] coof;
   public final int height;
   public final int width;

   public Matrix(double[][] coofs)
   {
      //System.out.println(coofs.height);

      if (coofs.length == 0)
      {
         throw new IllegalArgumentException("Пустая матрица не принимается.");
      }
      coof = Utils.copyOfMatrix(coofs);
      height = coof.length;
      width = coof[0].length;

   }

   public double[][] getCoof()
   {
      return coof;
   }

   public static Matrix fromString(String m) throws IllegalKeyException
   {
      System.out.println("Reading matrix from string:\n " + m);
      String[] str = m.split("\\n");
      if (str.length == 0)
      {
         return null;
      }
      String mat[][] = new String[str.length][];
      for (int i = 0; i < mat.length; ++i)
      {

         mat[i] = str[i].replaceAll("\\s+", "").split(",");
      }

      int height = mat.length;
      int width = mat[0].length;
      double[][] arr = new double[height][width];
      try
      {
         for (int i = 0; i < height; ++i)
            for (int j = 0; j < width; ++j)
               arr[i][j] = Double.valueOf(mat[i][j]);
      }
      catch (ArrayIndexOutOfBoundsException e)
      {
         e.printStackTrace();
         throw new IllegalKeyException("Ключ должан быть не вырожденной квадратной матрицей, определитель которой является взаимнопростым с размером алфавита.");
      }
      return new Matrix(arr);
   }

   private void getMinor(double mas[][], double p[][], int i, int j, int m)
   {
      int ki, kj, di, dj;
      di = 0;
      for (ki = 0; ki < m - 1; ki++)
      {
         if (ki == i)
         {
            di = 1;
         }
         dj = 0;
         for (kj = 0; kj < m - 1; kj++)
         {
            if (kj == j)
            {
               dj = 1;
            }
            p[ki][kj] = mas[ki + di][kj + dj];
         }
      }
   }

   public double Determinant(double mas[][], int m)
   {
      if (height != width)
      {
         throw new IllegalArgumentException("Матрица не квадратная");
      }
      double d = 0;
      int n;
      double p[][] = new double[m][m];

      n = m - 1;
      if (m < 1)
      {
         return -1;
      }
      if (m == 1)
      {
         d = mas[0][0];
         return (d);
      }
      if (m == 2)
      {
         d = mas[0][0] * mas[1][1] - (mas[1][0] * mas[0][1]);
         return (d);
      }

      int k = 1;
      for (int i = 0; i < m; i++)
      {
         getMinor(mas, p, i, 0, m);
         d = d + k * mas[i][0] * Determinant(p, n);
         k = -k;
      }

      return (d);
   }

   public Matrix getAlly()
   {
      double res[][] = new double[height][height];
      double p[][] = new double[height][height];
      int mi = -1;
      for (int i = 0; i < height; ++i)
      {


         int mj = -1;
         for (int j = 0; j < height; ++j)
         {
            getMinor(coof, p, i, j, height);
            double det = Determinant(p, height - 1);
            res[i][j] = (mi * mj * det);
            mj = -mj;
         }
         mi = -mi;
      }
      return new Matrix(res);
   }

   public Matrix transparent()
   {
      double res[][] = new double[height][height];
      for (int i = 0; i < height; ++i)
      {
         for (int j = 0; j < height; ++j)
         {
            res[i][j] = coof[j][i];
         }
      }
      return new Matrix(res);
   }

   public Matrix mult(double q)
   {
      double res[][] = new double[height][height];
      for (int i = 0; i < height; ++i)
      {
         for (int j = 0; j < height; ++j)
         {
            res[i][j] = coof[i][j] * q;
         }
      }
      return new Matrix(res);
   }

   public Matrix mult(Matrix other)
   {
      if (width != other.height)
      {
         throw new IllegalArgumentException("Количество столбцов левого операнда, должно быть равно количеству строк правого.");
      }

      double c[][] = new double[height][other.width];
      for (int i = 0; i < height; ++i)
      {
         for (int j = 0; j < other.width; ++j)
         {
            int sum = 0;
            for (int k = 0; k < width; ++k)
            {
               sum += coof[i][k] * other.coof[k][j];
            }
            c[i][j] = sum % Methods.chars.length();
            if (c[i][j] < 0)
               c[i][j] += Methods.chars.length();
         }
      }
      return new Matrix(c);
   }

   @Override
   public String toString()
   {
      if (coof.length == 0)
      {
         return "[]";
      }
      StringBuilder sb = new StringBuilder("======={").append(height).append("x").append(width).append("}========\n").append(Arrays.toString(coof[0]));
      for (int i = 1; i < coof.length; ++i)
      {
         sb.append(",\n").append(Arrays.toString(coof[i]));
      }
      return sb.append("\n===============").toString();
   }

}