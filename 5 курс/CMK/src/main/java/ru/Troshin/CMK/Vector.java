package ru.Troshin.CMK;

import java.util.Arrays;

public class Vector
{
   private double[] coords;
   public int getSize()
   {
      return coords.length;
   }
   public Vector(double[] coords)
   {
      this.coords = Arrays.copyOf(coords, coords.length);
   }
   public static Vector fromString(String t)
   {
      String[] r = t.replaceAll("\\s+","").split(",");
      double[] arr = new double[r.length];
      for(int i = 0; i < r.length; ++i)
         arr[i] = Double.valueOf(r[i]);
      return new Vector(arr);
   }
   public double get(int index)
   {
      return coords[index];
   }
   @Override
   public String toString()
   {
      return Arrays.toString(coords);
   }
   public Matrix toMatrix()
   {
      double[][] d = new double[coords.length][1];
      for(int i = 0; i < coords.length; ++i)
         d[i][0]=coords[i];
      return new Matrix(d);
   }
   public static Vector fromMatrix(Matrix m)
   {
      if(m.width!=1)
         throw new IllegalArgumentException("Принимается только матрица Nx1");
      double arr[] = new double[m.height];
      for(int i =0 ; i <m.height;++i)
         arr[i] = m.coof[i][0];
      return new Vector(arr);
   }
}
