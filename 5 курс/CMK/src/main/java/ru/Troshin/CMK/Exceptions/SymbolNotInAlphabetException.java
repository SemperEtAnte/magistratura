package ru.Troshin.CMK.Exceptions;

public class SymbolNotInAlphabetException extends Exception
{
    public SymbolNotInAlphabetException(String message)
    {
        super(message);
    }
    public SymbolNotInAlphabetException(char c)
    {
    	super("Символ " + c + " не является цифрой или буквой русского алфавита");
    }
}
