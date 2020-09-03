package ru.Troshin.CMK;

import ru.Troshin.CMK.Exceptions.IllegalKeyException;
import ru.Troshin.CMK.Exceptions.SymbolNotInAlphabetException;

public class Methods
{

    public static final String chars = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyz1234567890"; //Алфавит
    //public static final String chars = "абвгґдеєжзиіїйклмнопрстуфхцчшщьюя";

    @ShifMethod(name = "Решето Эратосфена")
    public String eratosfen(String line, String key, boolean crip) throws IllegalKeyException
    {
        try
        {
            int number = Integer.parseInt(line);
            if (number < 2)
            {
                return "";
            }
            boolean[] bools = new boolean[number];

            for (int i = 2; i * i < number; ++i)
            {
                if (!bools[i])
                {
                    int step = i == 2 ? i : i + i;
                    for (int j = i * i; j < number; j += step)
                    {
                        bools[j] = true;
                    }
                }
            }

            StringBuilder sb = new StringBuilder("2");
            for (int i = 3; i < bools.length; i += 2)
            {
                if (!bools[i])
                {

                    sb.append(", ").append(i);
                }
            }
            return sb.toString();

        }
        catch (NumberFormatException e)
        {
            throw new IllegalKeyException("Строка должна быть числом, которое будет принято за n");
        }
    }


    //=== 3 курс ===
    @ShifMethod(name = "Цезарь")
    public String ceaser(String line, String k, boolean crip) throws IllegalKeyException, SymbolNotInAlphabetException //line - строка, k - ключ, crip - шифровать (true) или дешифровать (false)
    {

        char[] ln = line.toCharArray(); // Беру массив символов исходной строки
        char[] res = new char[line.length()]; //Записывать результат сюда
        try
        {
            int key = Integer.parseInt(k); //Ключ

            if (crip)
            {
                for (int i = 0; i < ln.length; ++i)
                {
                    int code = chars.indexOf(ln[i]); //Код в алфавите
                    if (code == -1) //Если символ не в алфавите
                    {
                        throw new SymbolNotInAlphabetException("Символ " + ln[i] + " не является цифрой или буквой русского алфавита.");
                    }
                    code = (code + key) % chars.length(); //Вычисляем новый код по формуле
                    res[i] = chars.charAt(code); //Сохраняем в результат
                }
            }
            else
            {

                //Аналогично как и выше, только по-другому вычисляем новый код
                for (int i = 0; i < ln.length; ++i)
                {
                    int code = chars.indexOf(ln[i]);
                    code = (code - key) % chars.length();
                    if (code < 0)
                    {
                        code += chars.length();
                    }
                    res[i] = chars.charAt(code);
                }
            }

        }
        catch (NumberFormatException e)
        {
            throw new IllegalKeyException("Ключ должен быть целым числом");
        }

        return new String(res);
    }

    @ShifMethod(name = "Простая замена")
    public String per(String line, String key, boolean crip) throws IllegalKeyException, SymbolNotInAlphabetException
    {
        if (chars.length() > key.length())
        {
            throw new IllegalKeyException("Ключ должен быть строкой длиной " + chars.length() + " симв.");
        }
        char[] ln = line.toCharArray();
        StringBuilder res = new StringBuilder(); //Результат
        if (crip) //Если шифруем
        {
            for (char c : ln) //Переставляем
            {
                int index = chars.indexOf(c);
                if (index == -1)
                {
                    throw new SymbolNotInAlphabetException(c);
                }
                res.append(key.charAt(index));
            }
        }
        else //Иначе дешифруем
        {
            for (char c : ln)
            {
                int index = key.indexOf(c);
                if (index == -1)
                {
                    throw new SymbolNotInAlphabetException(c);
                }
                res.append(chars.charAt(index));
            }
        }
        return res.toString();

    }

    @ShifMethod(name = "Виженера")
    public String Vizh(String line, String key, boolean crip) throws IllegalKeyException, SymbolNotInAlphabetException
    {

        StringBuilder sb = new StringBuilder();
        int repIndex = 0;
        if (key == null || key.isEmpty())
        {
            throw new IllegalKeyException("Необходим ключ, являющийся словом");
        }
        if (crip)
        {
            for (char c : line.toCharArray())
            {
                if (repIndex == key.length())
                {
                    repIndex = 0;
                }
                int code = chars.indexOf(c);
                if (code < 0)
                {
                    throw new SymbolNotInAlphabetException("Символ '" + c + "' не является буквой русского алфавита.");
                }
                code = (code + chars.indexOf(key.charAt(repIndex++))) % chars.length();
                sb.append(chars.charAt(code));
            }

        }
        else
        {
            for (char c : line.toCharArray())
            {
                if (repIndex == key.length())
                {
                    repIndex = 0;
                }
                int code = chars.indexOf(c);
                code = (code - chars.indexOf(key.charAt(repIndex++))) % chars.length();
                if (code < 0)
                {
                    code += chars.length();
                }
                sb.append(chars.charAt(code));
            }
        }
        return sb.toString();
    }

    @ShifMethod(name = "Общий перестановки")
    public String Obsh_Per(String line, String replaces, boolean crip) throws IllegalKeyException
    {
        // 3 1 4 2 | 2 0 3 1
        String[] args = replaces.replaceAll("\\s+", "").split(",");
        int[] keys = new int[args.length];
        try
        {
            for (int i = 0; i < args.length; ++i)
            {
                keys[i] = Integer.valueOf(args[i]);
            }
        }
        catch (NumberFormatException e)
        {
            throw new IllegalKeyException("Ключ должен быть массивом чисел указанных через запятую");
        }
        while (line.length() % keys.length != 0)
        {
            line += '0';
        }
        char[] chars = line.toCharArray();
        char[] replaced = new char[chars.length];
        if (crip)
        {

            for (int i = 0; i < chars.length - keys.length + 1; i += keys.length)
            {
                for (int j = 0; j < keys.length; ++j)
                {
                    replaced[i + keys[j]] = chars[i + j];
                }
            }
        }
        else
        {
            for (int i = 0; i < chars.length - keys.length + 1; i += keys.length)
            {
                for (int j = 0; j < keys.length; ++j)
                {
                    replaced[i + j] = chars[i + keys[j]];
                }
            }
        }

        String res = new String(replaced);

        return res;
    }

    @ShifMethod(name = "Афинный первого порядка")
    public String afinn(String line, String key, boolean crip) throws IllegalKeyException, SymbolNotInAlphabetException
    {
        String[] ks = key.split("\\,");
        try
        {

            StringBuilder sb = new StringBuilder();
            int a = Integer.valueOf(ks[0]);
            int s = Integer.valueOf(ks[1]);
            if (a < 0 || a >= chars.length() || s < 0 || s >= chars.length() || Utils.getNOD(a, chars.length()) != 1)
            {
                throw new IllegalKeyException("Ключом должны быть 2 целых числа, указанны через запятую. Причем числа больше нуля и меньше " + chars.length() + " и первое из них является взаимнопростым для " + chars.length());
            }
            if (crip)
            {
                for (char c : line.toCharArray())
                {
                    int code = chars.indexOf(c);
                    if (code == -1)
                    {
                        throw new SymbolNotInAlphabetException("Символ " + c + " не является цифрой или буквой русского алфавита.");
                    }
                    code = (code * a + s) % chars.length();
                    sb.append(chars.charAt(code));
                }
            }
            else
            {
                int ash = Utils.getReversedElement(chars.length(), a);
                if (ash == -1)
                {
                    throw new IllegalKeyException("Ключом должны быть 2 целых числа, указанны через запятую. Причем числа больше нуля и меньше " + chars.length() + " и первое из них является взаимнопростым для " + chars.length());
                }

                int ssh = (-ash * s) % chars.length();
                for (char c : line.toCharArray())
                {
                    int code = chars.indexOf(c);
                    if (code == -1)
                    {
                        throw new SymbolNotInAlphabetException("Символ " + c + " не является цифрой или буквой русского алфавита.");
                    }
                    code = (code * ash + ssh) % chars.length();
                    sb.append(chars.charAt(code));
                }
            }

            return sb.toString();
        }
        catch (NumberFormatException | ArrayIndexOutOfBoundsException e)
        {
            throw new IllegalKeyException("Ключом должны быть 2 целых числа, указанны через запятую. Причем числа больше нуля и меньше " + chars.length() + " и первое из них является взаимнопростым для " + chars.length());
        }

    }

    @ShifMethod(name = "Аффинный к-го порядка.")
    public String afinnK(String line, String key, boolean crip) throws IllegalKeyException, SymbolNotInAlphabetException
    {

        StringBuilder sbl = new StringBuilder();
        String[] spl = key.split("\n");
//      for(int i = 0; i <2; ++i)
//         sbl.append(spl[i]).append("\n");
        Matrix A = Matrix.fromString(key);
        //Vector V =Vector.fromString(spl[2]);
        double determ = A.Determinant(A.coof, A.width) % chars.length();

        if (determ == 0)
        {
            throw new IllegalKeyException("Матрица является вырожденной");
        }
        double w = determ % chars.length();
        if (w < 0)
        {
            w += chars.length();
        }
        if (Utils.getNOD((int) w, chars.length()) != 1)
        {
            throw new IllegalKeyException("Опредетилель матрицы не является взаимнопростым размеру алфавита. det(A) = " + determ + " size = " + chars.length());
        }

        int n = A.width;
        while (line.length() % n != 0)
        {
            line += "б";
        }
        double[][] lines = new double[n][line.length() / n];
        for (int i = 0, k = 0; i < line.length() / n; ++i, k += n)
        {
            for (int j = 0; j < n; ++j)
            {
                lines[j][i] = chars.indexOf(line.charAt(k + j));
                if (lines[j][i] == -1)
                {
                    throw new SymbolNotInAlphabetException("Символ " + line.charAt(k + j) + " не является буквой русского алфавита или цифрой.");
                }
            }
        }

        Matrix X = new Matrix(lines);

        Matrix res;

        if (crip)
        {
            res = A.mult(X);
        }
        else
        {
            double wsh = Utils.getReversedElement((int) w, chars.length());
            Matrix ally = A.getAlly();
            Matrix tr = ally.transparent();
            Matrix Ash = tr.mult(wsh);
            res = Ash.mult(X);
        }
        double[][] r = res.coof;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < r[0].length; ++i)
        {
            for (int j = 0; j < r.length; ++j)
            {
                sb.append(chars.charAt((int) r[j][i]));
            }
        }
        return sb.toString();
    }

}
