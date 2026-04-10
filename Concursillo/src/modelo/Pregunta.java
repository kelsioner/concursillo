package modelo;

public class Pregunta {

    private String pregunta;
    private String opcionA;
    private String opcionB;
    private String opcionC;
    private String opcionD;
    private String correcta;
    private int nivel;
    private String pista;
    private String categoria;

    public Pregunta() {
    }

    public Pregunta(String pregunta, String opcionA, String opcionB, String opcionC, String opcionD,
                    String correcta, int nivel, String pista, String categoria) {
        this.pregunta = pregunta;
        this.opcionA = opcionA;
        this.opcionB = opcionB;
        this.opcionC = opcionC;
        this.opcionD = opcionD;
        this.correcta = correcta;
        this.nivel = nivel;
        this.pista = pista;
        this.categoria = categoria;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getOpcionA() {
        return opcionA;
    }

    public void setOpcionA(String opcionA) {
        this.opcionA = opcionA;
    }

    public String getOpcionB() {
        return opcionB;
    }

    public void setOpcionB(String opcionB) {
        this.opcionB = opcionB;
    }

    public String getOpcionC() {
        return opcionC;
    }

    public void setOpcionC(String opcionC) {
        this.opcionC = opcionC;
    }

    public String getOpcionD() {
        return opcionD;
    }

    public void setOpcionD(String opcionD) {
        this.opcionD = opcionD;
    }

    public String getCorrecta() {
        return correcta;
    }

    public void setCorrecta(String correcta) {
        this.correcta = correcta;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public String getPista() {
        return pista;
    }

    public void setPista(String pista) {
        this.pista = pista;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Pregunta otra = (Pregunta) obj;
        return this.pregunta.equals(otra.pregunta);
    }

    @Override
    public String toString() {
        return "Pregunta: " + pregunta;
    }
}