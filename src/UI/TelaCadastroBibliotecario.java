// package UI;

// import java.util.List;
// import java.util.Scanner;
// import entities.Bibliotecario;
// import entities.Usuario;
// import interfaces.Cargo;

// public class TelaCadastroBibliotecario extends TelaCadastroBase {

//     public TelaCadastroBibliotecario(Scanner sc) {
//         super(sc);
//     }

//     @Override
//     protected String tituloTela() {
//         return "CADASTRO DE BIBLIOTECÁRIO";
//     }

//     // @Override
//     // protected Usuario criarUsuario() {
//     //     DadosBasicos dados = lerDadosBasicos();
//     //     String cargo = lerCargo();
//     //     return new Bibliotecario(dados.nome(), dados.cpf(), dados.login(), dados.senha(), cargo);
//     // }

//     private String lerCargo() {
//         Cargo[] cargos = Cargo.values();
//         System.out.println("Cargo:");
//         for (int i = 0; i < cargos.length; i++) {
//             System.out.println("  " + (i + 1) + " - " + cargos[i]);
//         }
//         int opcao = lerOpcao();
//         if (opcao < 1 || opcao > cargos.length) {
//             throw new IllegalArgumentException("Cargo inválido.");
//         }
//         return cargos[opcao - 1].name();
//     }
// }
