package entidades;

public enum Denominacion
{
    MIL(1000), QUINIENTOS(500);
    private final double numero;

    Denominacion(double numero)
    {
        this.numero = numero;
    }

    public double retornarNumero()
    {
        return numero;
    }
}
