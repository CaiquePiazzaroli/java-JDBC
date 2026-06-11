import java.time.LocalDate;

public class Cliente {

    private String nome;
    private LocalDate dataNascimento;
    private Double pontuacao;
    private Long cpf;

    public Cliente(){};

    public Cliente(String nome, LocalDate dataNascimento, Double pontuacao, Long cpf) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.pontuacao = pontuacao;
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public Double getPontuacao() {
        return pontuacao;
    }

    public Long getCpf() {
        return cpf;
    }

    public void setCpf(Long cpf) {
        this.cpf = cpf;
    }

    public void setPontuacao(Double pontuacao) {
        this.pontuacao = pontuacao;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
