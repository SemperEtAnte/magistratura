package ru.Troshin.CMK.Exceptions;

public class IllegalKeyException extends Exception
{
    public IllegalKeyException(String s)//ѕередаем в родительский класс сообщение
    {
        super(s);
    }
}
