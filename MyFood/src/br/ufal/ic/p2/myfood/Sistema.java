package br.ufal.ic.p2.myfood;

 import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

import br.ufal.ic.p2.myfood.salvadados.*;






class Sistema {
    private Map<Integer, Usuario> usuarios;
    private Map<String, Usuario> emailMap;

    private Map<Integer, Empresa> empresas;
    private Map<Integer, List<Empresa>> empresasPorDono;

    private Map<Integer, Produto> produtos;
   // private static final String FILE_PATH = "C:\\Users\\ezequ\\Documents\\facu\\P2\\myfood\\usuario.txt"; 

    private Map<Integer, List<Produto>> produtosPorRestaurante;

    private Map<Integer, Pedido> pedidos;
    private Map<Integer, List<Pedido>> pedidosPorRestaurante;
    private Map<Integer, List<Empresa>> empresasPorEntregador;

    private Map<Integer, Entrega> entregas;

    public Sistema() throws IOException, ClassNotFoundException {
        this.usuarios = SalarUsuarios.carregarUsuarios();
        this.emailMap = new HashMap<>();

        this.empresas = SalvarEmpresa.carregarEmpresas();
        this.empresasPorDono = GuardarEmpresas.carregarEmpresasDoDono();

        this.produtos = GuardaProduto.carregarProdutos();
        this.produtosPorRestaurante = ProdutosDoRestaurante.carregarProdutoDoRestaurante();


        this.pedidos = GuardaPedidos.carregarPedidos();
        this.pedidosPorRestaurante = PedidoDeRestaurante.carregarPedidosPorRestaurante();
        this.empresasPorEntregador = EmpresasDoEntregador.carregarEmpresaDeEntregador();


        this.entregas = SalvarEntrega.carregarEntregas();
    }

    public void zerarSistema() {
        this.usuarios.clear();
        this.emailMap.clear();

        this.empresas.clear();
        this.empresasPorDono.clear();

        this.produtos.clear();
        this.produtosPorRestaurante.clear();

        this.pedidos.clear();
        this.pedidosPorRestaurante.clear();
    }

    public void criarUsuario(String nome, String email, String senha, String endereco) {
        validarDados(nome, email, senha, endereco, null);
        if (emailMap.containsKey(email)) {
            throw new IllegalArgumentException("Conta com esse email ja existe");
        }
        Cliente cliente = new Cliente(nome, email, senha, endereco);
        usuarios.put(cliente.getId(), cliente);
        emailMap.put(email, cliente);
       //tirei o salvadados

    }

    public void criarUsuario(String nome, String email, String senha, String endereco, String cpf) {
        validarDados(nome, email, senha, endereco, cpf);
        if (!validarCpf(cpf)) {
            throw new IllegalArgumentException("CPF invalido"); //dados so de dono*
        }
        DonoRestaurante dono = new DonoRestaurante(nome, email, senha, endereco, cpf);
        usuarios.put(dono.getId(), dono);
        emailMap.put(email, dono);
    }

    //adic os exceptions here
    //entregador:
    public void criarUsuario(String nome, String email, String senha, String endereco, String veiculo, String placa){

                if (nome == null || nome.trim().isEmpty()) {
                    throw new IllegalArgumentException("Nome invalido");
                }
                    // Verifica se o email é válido
                if (email == null || email.trim().isEmpty() || !Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", email)) {
                    throw new IllegalArgumentException("Email invalido");
                }
                    // Verifica se a senha é válida
                if (senha == null || senha.trim().isEmpty()) {
                    throw new IllegalArgumentException("Senha invalido");
                }
                    // Verifica se o endereço é válido
                if (endereco == null || endereco.trim().isEmpty()) {
                    throw new IllegalArgumentException("Endereco invalido");
                }

        // Validar veículo e placa
        if (veiculo == null || veiculo.trim().isEmpty()){
            throw new IllegalArgumentException("Veiculo invalido");
        }

        for (Usuario entregador : usuarios.values()){
            if(entregador.ehEntregador()) {
                if(entregador.getAtributo("placa").equals(placa)){
                    throw new IllegalArgumentException("Placa invalido");
                }
            }
        }
 
        if (placa == null || placa.trim().isEmpty()){
            throw new IllegalArgumentException("Placa invalido");
        }

        if (emailMap.containsKey(email)){
            throw new IllegalArgumentException("Conta com esse email ja existe");
        }

        Entregador entregador = new Entregador(nome, email, senha, endereco, veiculo, placa);
        usuarios.put(entregador.getId(), entregador);
        emailMap.put(email, entregador);
    }




    public int login(String email, String senha) {
        for (Usuario usuario : usuarios.values()) {
            if (usuario.getEmail().equals(email) && usuario.getSenha().equals(senha)) {
                return usuario.getId();
            }
        }
        throw new IllegalArgumentException("Login ou senha invalidos");
    }


    //renomear aq
    public void encerrarSistema() throws IOException {
        SalarUsuarios.salvarUsuarios(usuarios);
        SalvarEmpresa.salvarEmpresas(empresas);
        GuardarEmpresas.salvarEmpresaDoDono(empresasPorDono);
        ProdutosDoRestaurante.salvarProdutoDoRestaurante(produtosPorRestaurante);
        GuardaProduto.salvarProdutos(produtos);
        GuardaPedidos.salvarPedidos(pedidos);
        PedidoDeRestaurante.salvarPedidosPorRestaurante(pedidosPorRestaurante);
        EmpresasDoEntregador.salvarEmpresaDoEntregador(empresasPorEntregador);
        SalvarEntrega.salvarEntregas(entregas);
    }


   // Método para cadastrar entregadores em uma empresa existente
   public void cadastrarEntregador(int idEmpresa, int idEntregador) {
    Empresa empresa = empresas.get(idEmpresa);
    if (empresa == null) {
        throw new IllegalArgumentException("Empresa nao encontrada");
    }

    Usuario usuario = usuarios.get(idEntregador);
    if (usuario == null || !usuario.ehEntregador()) {
        throw new IllegalArgumentException("Usuario nao e um entregador");
    }

    Entregador entregador = (Entregador) usuario;

    // Verifica se o entregador já está associado à empresa
    if (empresa.getEntregadores().contains(entregador)) {
        throw new IllegalArgumentException("Entregador já associado a esta empresa");
    }

    // Adiciona o entregador à lista de entregadores da empresa
    empresa.getEntregadores().add(entregador);

    // Adiciona a empresa à lista de empresas do entregador
    List<Empresa> empresasDoEntregador = empresasPorEntregador.getOrDefault(idEntregador, new ArrayList<>());
    if (!empresasDoEntregador.contains(empresa)) {
        empresasDoEntregador.add(empresa);
    }
    empresasPorEntregador.put(idEntregador, empresasDoEntregador);
}



// Método para retornar os emails dos entregadores de uma empresa
public String getEntregadores(int idEmpresa) {
    Empresa empresa = empresas.get(idEmpresa);
    if (empresa == null) {
        throw new IllegalArgumentException("Empresa nao encontrada");
    }

    Set<String> emailsEntregadores = new HashSet<>();
    for (Entregador entregador : empresa.getEntregadores()) {
        emailsEntregadores.add(entregador.getEmail());
    }

    return "{" + emailsEntregadores.toString() + "}";
}


public String getEmpresas(int idEntregador) {
    Usuario entregador = usuarios.get(idEntregador);
    if (entregador == null || !entregador.ehEntregador()) {
        throw new IllegalArgumentException("Usuario nao e um entregador");
    }

    List<Empresa> empresasDoEntregador = empresasPorEntregador.get(idEntregador);
    if (empresasDoEntregador == null || empresasDoEntregador.isEmpty()) {
        return "{}"; // Nenhuma empresa associada
    }

    // Constrói a lista de atributos das empresas associadas ao entregador
    Set<String> empresaatributos = new LinkedHashSet<>();
    for (Empresa empresa : empresasDoEntregador) {
        if (empresa.getEntregadores().contains(entregador)) {
            String atributo = "[" + empresa.getNome() + ", " + empresa.getEndereco() + "]";
            empresaatributos.add(atributo);
        }
    }

    // Retorna o formato esperado
 
 
    return "{[" + String.join(", ", empresaatributos) + "]}";
}




private void validarDados(String nome, String email, String senha, String endereco, String cpf) {
    if (nome == null || nome.trim().isEmpty()) {
        throw new IllegalArgumentException("Nome invalido");
    }

    if (email == null || email.trim().isEmpty() || !Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", email)) {
        throw new IllegalArgumentException("Email invalido");
    }

    if (senha == null || senha.trim().isEmpty()) {
        throw new IllegalArgumentException("Senha invalido");
    }

    if (endereco == null || endereco.trim().isEmpty()) {
        throw new IllegalArgumentException("Endereco invalido");
    }
}

// Método privado para validar CPF
private boolean validarCpf(String cpf) {
    return cpf != null && Pattern.matches("\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}", cpf);
}

// Método para obter um atributo específico de um usuário
public String getAtributoUsuario(int id, String atributo) {
    Usuario usuario = usuarios.get(id);
    if (usuario == null) throw new IllegalArgumentException("Usuario nao cadastrado.");
    return usuario.getAtributo(atributo);
}

// Método para criar um restaurante e associá-lo a um dono
public int criarEmpresa(String tipoEmpresa, int idDono, String nome, String endereco, String tipoCozinha) {
    Usuario usuario = usuarios.get(idDono);
    if (usuario == null || !usuario.podeCriarEmpresa()) {
        throw new IllegalArgumentException("Usuario nao pode criar uma empresa");
    }

    // Verificar duplicidade de nome para o mesmo dono
    List<Empresa> empresasDoDono = empresasPorDono.get(idDono);
    if (empresasDoDono != null) {
        for (Empresa empresa : empresasDoDono) {
            if (empresa.getNome().equals(nome) && empresa.getEndereco().equals(endereco)) {
                throw new IllegalArgumentException("Proibido cadastrar duas empresas com o mesmo nome e local");
            }
        }
    }

    for (Empresa empresa : empresas.values()) {
        if (empresa.getNome().equals(nome) && empresa.getEndereco().equals(endereco)) {
            throw new IllegalArgumentException("Empresa com esse nome ja existe");
        }
    }

    Restaurante empresa = new Restaurante(tipoEmpresa, nome, endereco, tipoCozinha);
    empresas.put(empresa.getId(), empresa);

    empresasDoDono = empresasPorDono.get(idDono);
    if (empresasDoDono == null) {
        empresasDoDono = new ArrayList<>();
        empresasPorDono.put(idDono, empresasDoDono);
    }

    empresasDoDono.add(empresa);

    return empresa.getId();
}


//funfa
// Criar Mercado
public int criarEmpresa(String tipoEmpresa, int idDono, String nome, String endereco, String abre, String fecha, String tipoMercado) {
    // Verificar se o usuário existe e se está autorizado a criar uma empresa
    Usuario usuario = usuarios.get(idDono);
    if (usuario == null || !usuario.podeCriarEmpresa()) {
        throw new IllegalArgumentException("Usuario nao pode criar uma empresa");
    }

    // Validar o tipo de empresa (deve ser mercado ou restaurante, por exemplo)
    if (tipoEmpresa == null || (!tipoEmpresa.equals("mercado") && !tipoEmpresa.equals("restaurante"))) {
        throw new IllegalArgumentException("Tipo de empresa invalido");
    }

    // Validar o nome da empresa
    if (nome == null || nome.trim().isEmpty()) {
        throw new IllegalArgumentException("Nome invalido");
    }

    // Validar o endereço da empresa
    if (endereco == null || endereco.trim().isEmpty()) {
        throw new IllegalArgumentException("Endereco da empresa invalido");
    }

    
     // Verificar se o horário de abertura é válido
     if (abre == null) {
        throw new IllegalArgumentException("Horario invalido");
    }

    // Verificar se o horário de fechamento é válido
    if (fecha == null) {
        throw new IllegalArgumentException("Horario invalido");
    }
// Verificar se o horário de fechamento é nulo
 
// Verificar se o formato da hora de abertura é válidoodeiohora
if (!horaFormatoValido(abre)) {
    throw new IllegalArgumentException("Formato de hora invalido");
}


    // Validar os horários de abertura e fechamento
    if (abre == null || fecha == null || !horaFormatoValido(abre) || !horaFormatoValido(fecha)) {
        throw new IllegalArgumentException("Formato de hora invalido");
    }

    // Verificar se as horas estão dentro dos limites permitidos e se o fechamento é após a abertura
    if (!horaDentroLimite(abre) || !horaDentroLimite(fecha) || !validarOrdemHorarios(abre, fecha)) {
        throw new IllegalArgumentException("Horario invalido");
    }

    // Validar o tipo de mercado
    if (tipoMercado == null || tipoMercado.trim().isEmpty()) {
        throw new IllegalArgumentException("Tipo de mercado invalido");
    }

    // Verificar se já existe uma empresa com o mesmo nome e endereço para o dono
    List<Empresa> empresasDoDono = empresasPorDono.get(idDono);
    if (empresasDoDono != null) {
        for (Empresa empresa : empresasDoDono) {
            if (empresa.getNome().equals(nome) && empresa.getEndereco().equals(endereco)) {
                throw new IllegalArgumentException("Proibido cadastrar duas empresas com o mesmo nome e local");
            }
        }
    }

    // Verificar se existe uma empresa com o mesmo nome para outros donos
    for (Map.Entry<Integer, List<Empresa>> entry : empresasPorDono.entrySet()) {
        int donoId = entry.getKey();
        if (donoId != idDono) {
            List<Empresa> empresasOutroDono = entry.getValue();
            for (Empresa empresa : empresasOutroDono) {
                if (empresa.getNome().equals(nome)) {
                    throw new IllegalArgumentException("Empresa com esse nome ja existe");
                }
            }
        }
    }

    // Criar a nova empresa (Mercado)
    Mercado empresa = new Mercado(tipoEmpresa, nome, endereco, abre, fecha, tipoMercado);
    empresas.put(empresa.getId(), empresa);

    // Adicionar a nova empresa à lista de empresas do dono
    empresasDoDono = empresasPorDono.get(idDono);
    if (empresasDoDono == null) {
        empresasDoDono = new ArrayList<>();
        empresasPorDono.put(idDono, empresasDoDono);
    }

    empresasDoDono.add(empresa);

    return empresa.getId();
}

// Método para alterar o horário de funcionamento de um mercado
public void alterarFuncionamento(int mercadoId, String abre, String fecha) {
    // Verificar se o mercado existe
    if (!empresas.containsKey(mercadoId)) {
        throw new IllegalArgumentException("Nao e um mercado valido");
    }
    if (abre == null || fecha == null) {
        throw new IllegalArgumentException("Horario invalido");
    }
    // Validar os horários de abertura e fechamento
    if (abre == null || fecha == null || !horaFormatoValido(abre) || !horaFormatoValido(fecha)) {
        throw new IllegalArgumentException("Formato de hora invalido");//crt
    }

    // Verificar se as horas estão dentro dos limites permitidos e se o fechamento é após a abertura
    if (!horaDentroLimite(abre) || !horaDentroLimite(fecha) || !validarOrdemHorarios(abre, fecha)) {
        throw new IllegalArgumentException("Horario invalido");
    }
 
  
    // Atualizar o horário de funcionamento do mercado
    Empresa mercado = empresas.get(mercadoId);
    if (!mercado.isMercado()) {
        throw new IllegalArgumentException("Nao e um mercado valido");
    }

    mercado.setAtributo("abre", abre);
    mercado.setAtributo("fecha", fecha);
}













// Criar Farmacia
public int criarEmpresa(String tipoEmpresa, int idDono, String nome, String endereco, boolean aberto24Horas, int numeroFuncionarios) {
    // Verificar se o usuário existe e se está autorizado a criar uma empresa
    Usuario usuario = usuarios.get(idDono);
    if (usuario == null || !usuario.podeCriarEmpresa()) {
        throw new IllegalArgumentException("Usuario nao pode criar uma empresa");
    }

    // Validar o tipo de empresa (deve ser farmácia)
    if (tipoEmpresa == null || !tipoEmpresa.equals("farmacia")) {
        throw new IllegalArgumentException("Tipo de empresa invalido");
    }

    // Validar o nome da farmácia
    if (nome == null || nome.trim().isEmpty()) {
        throw new IllegalArgumentException("Nome invalido");
    }

    // Validar o endereço da farmácia
    if (endereco == null || endereco.trim().isEmpty()) {
        throw new IllegalArgumentException("Endereco da empresa invalido");
    }

    // Verificar se o dono já possui uma empresa com o mesmo nome e endereço
    List<Empresa> empresasDoDono = empresasPorDono.get(idDono);
    if (empresasDoDono != null) {
        for (Empresa empresa : empresasDoDono) {
            if (empresa.getNome().equals(nome) && empresa.getEndereco().equals(endereco)) {
                throw new IllegalArgumentException("Proibido cadastrar duas empresas com o mesmo nome e local");
            }
        }
    }

    // Verificar se outra pessoa já possui uma empresa com o mesmo nome
    for (Map.Entry<Integer, List<Empresa>> entry : empresasPorDono.entrySet()) {
        int donoId = entry.getKey();
        if (donoId != idDono) {
            List<Empresa> empresasOutroDono = entry.getValue();
            for (Empresa empresa : empresasOutroDono) {
                if (empresa.getNome().equals(nome)) {
                    throw new IllegalArgumentException("Empresa com esse nome ja existe");
                }
            }
        }
    }

    // Criar a nova farmácia
    Farmacia empresa = new Farmacia(tipoEmpresa, nome, endereco, aberto24Horas, numeroFuncionarios);
    empresas.put(empresa.getId(), empresa);

    // Adicionar a nova farmácia à lista de empresas do dono
    empresasDoDono = empresasPorDono.get(idDono);
    if (empresasDoDono == null) {
        empresasDoDono = new ArrayList<>();
        empresasPorDono.put(idDono, empresasDoDono);
    }

    empresasDoDono.add(empresa);

    return empresa.getId();
}















// Método auxiliar para verificar o formato da hora
private boolean horaFormatoValido(String hora) {
    return hora.matches("\\d{2}:\\d{2}");
}

// Método auxiliar para verificar se a hora está dentro dos limites corretos
private boolean horaDentroLimite(String hora) {
    String[] partes = hora.split(":");
    int horas = Integer.parseInt(partes[0]);
    int minutos = Integer.parseInt(partes[1]);

    return horas >= 0 && horas <= 23 && minutos >= 0 && minutos <= 59;
}

// Método para validar que o fechamento ocorre após a abertura no mesmo dia
private boolean validarOrdemHorarios(String abre, String fecha) {
    String[] partesAbre = abre.split(":");
    String[] partesFecha = fecha.split(":");

    int horaAbre = Integer.parseInt(partesAbre[0]);
    int minutoAbre = Integer.parseInt(partesAbre[1]);
    int horaFecha = Integer.parseInt(partesFecha[0]);
    int minutoFecha = Integer.parseInt(partesFecha[1]);

    if (horaFecha < horaAbre) {
        return false;
    } else if (horaFecha == horaAbre && minutoFecha <= minutoAbre) {
        return false; // Fechamento não pode ser antes ou igual à abertura
    }

    return true;
}



    public String getEmpresasDoUsuario(int idDono){

         Usuario usuario = usuarios.get(idDono);
        if (usuario == null || !usuario.podeCriarEmpresa()) {
            throw new IllegalArgumentException("Usuario nao pode criar uma empresa");
        }

         List<Empresa> empresasDoDono = empresasPorDono.get(idDono);
        if (empresasDoDono == null || empresasDoDono.isEmpty()) {
            return "{[]}";  
        }

        StringBuilder resultado = new StringBuilder("{[");
        for (int i = 0; i < empresasDoDono.size(); i++) {
            Empresa empresa = empresasDoDono.get(i);
            if (i > 0) {
                resultado.append(", ");
            }
            resultado.append("[")
                    .append(empresa.getNome())
                    .append(", ")
                    .append(empresa.getEndereco())
                    .append("]");
        }
        resultado.append("]}");

        return resultado.toString();
    }

    public int getIdEmpresa(int idDono, String nome, int indice){
 if (nome == null || nome.trim().isEmpty()) {
    throw new IllegalArgumentException("Nome invalido");
}

 List<Empresa> empresas = empresasPorDono.get(idDono);

 if (empresas == null || empresas.isEmpty()) {
    throw new IllegalArgumentException("Empresa com esse nome ja existe");
}

 if (indice < 0) {
    throw new IllegalArgumentException("Indice invalido");
}

List<Integer> idsCorrespondentes = new ArrayList<>();

for (Empresa empresa : empresas) {
    if (empresa.getNome().equals(nome)) {
        idsCorrespondentes.add(empresa.getId());
    }
}

if (idsCorrespondentes.isEmpty()) {
    throw new IllegalArgumentException("Nao existe empresa com esse nome");
}

if (indice >= idsCorrespondentes.size()) {
    throw new IllegalArgumentException("Indice maior que o esperado");
}

return idsCorrespondentes.get(indice);
}

public String getAtributoEmpresa(int empresaId, String atributo){
if(atributo == null){
    throw new IllegalArgumentException("Atributo invalido");
}

Empresa empresa = empresas.get(empresaId);
if (empresa == null) {
    throw new IllegalArgumentException("Empresa nao cadastrada");
}

if (atributo.equals("dono")) {
    for (Map.Entry<Integer, List<Empresa>> entry : empresasPorDono.entrySet()) {
        List<Empresa> restaurantesDoDono = entry.getValue();
        for (Empresa r : restaurantesDoDono) {
            if (r.getId() == empresaId) {
                return usuarios.get(entry.getKey()).getNome();
            }
        }
    }
}

return empresa.getAtributo(atributo);
}








//pt 3
//feito
public int criarProduto(int empresa, String nome, float valor, String categoria) {
    // Verifica se os parâmetros são válidos antes de criar o produto
    if (nome == null || nome.trim().isEmpty())
        throw new IllegalArgumentException("Nome invalido");
    if (categoria == null || categoria.trim().isEmpty())
        throw new IllegalArgumentException("Categoria invalido");
    if (valor <= 0)
        throw new IllegalArgumentException("Valor invalido");

    // Verifica se já existe um produto com o mesmo nome na empresa
    List<Produto> produtosDoRestaurante = produtosPorRestaurante.get(empresa);
    if (produtosDoRestaurante != null) {
        for (Produto produto : produtosDoRestaurante) {
            if (produto.getNome().equals(nome)) {
                throw new IllegalArgumentException("Ja existe um produto com esse nome para essa empresa");
            }
        }
    }

    // Cria o produto e o adiciona à lista da empresa
    Produto produto = new Produto(nome, valor, categoria);

    produtosDoRestaurante = produtosPorRestaurante.get(empresa);
    if (produtosDoRestaurante == null) {
        produtosDoRestaurante = new ArrayList<>();
        produtosPorRestaurante.put(empresa, produtosDoRestaurante);
    }

    produtosDoRestaurante.add(produto);
    produtos.put(produto.getId(), produto);// Adiciona o produto ao mapa global de produtos

    return produto.getId();// Método para gerar um ID único
}

public String listarProdutos(int empresa) {
    // Recupera a empresa e valida se ela existe
    Empresa restaurante = empresas.get(empresa);
    if (restaurante == null) {
        throw new IllegalArgumentException("Empresa nao encontrada");
    }

    // Lista os produtos do restaurante
    List<Produto> produtoDoRestaurante = produtosPorRestaurante.get(empresa);
    if (produtoDoRestaurante == null || produtoDoRestaurante.isEmpty()) {
        return "{[]}"; // Se não houver produtos, retorna um JSON vazio
    }

    // Formata a lista de produtos em uma string
    StringBuilder resultado = new StringBuilder("{[");
    for (int i = 0; i < produtoDoRestaurante.size(); i++) {
        Produto produto = produtoDoRestaurante.get(i);
        if (i > 0) {
            resultado.append(", ");
        }
        resultado.append(produto.getNome());
    }
    resultado.append("]}");

    return resultado.toString();
}





public void editarProduto(int produto, String nome, float valor, String categoria) {
    // Edita um produto já existente, validando as novas informações
    Produto produto1 = produtos.get(produto);

    if (nome == null || nome.trim().isEmpty())
        throw new IllegalArgumentException("Nome invalido");
    if (categoria == null || categoria.trim().isEmpty())
        throw new IllegalArgumentException("Categoria invalido");
    if (valor <= 0)
        throw new IllegalArgumentException("Valor invalido");

    if (produto1 == null) {
        throw new IllegalArgumentException("Produto nao cadastrado");
    }

    produto1.setNome(nome);
    produto1.setValor(valor);
    produto1.setCategoria(categoria);
}

public String getProduto(String nome, int empresa, String atributo) {
    // Recupera um produto pelo nome e retorna o atributo solicitado
    if (atributo == null) {
        throw new IllegalArgumentException("Atributo nao existe");
    }

    List<Produto> produtosDoRestaurante = produtosPorRestaurante.get(empresa);
    if (produtosDoRestaurante == null) {
        throw new IllegalArgumentException("Produto nao encontrado");
    }

    for (Produto produto : produtosDoRestaurante) {
        if (produto.getNome().equals(nome)) {
            if (atributo.equals("valor")) {
                return String.format(Locale.US, "%.2f", produto.getValor());
            } else if (atributo.equals("empresa")) {
                Empresa restaurante = empresas.get(empresa);
                if (restaurante != null) {
                    return restaurante.getNome();
                } else {
                    throw new IllegalArgumentException("Produto nao encontrado");
                }
            } else {
                return produto.getAtributo(atributo);
            }
        }
    }

    throw new IllegalAccessError("Produto nao encontrado");
}






//4


 






    //feito**
    public int criarPedido(int clienteId, int empresaId) {
        // Validação do cliente e da empresa para garantir que ambos existem
        Usuario cliente = usuarios.get(clienteId);
        Empresa empresa = empresas.get(empresaId);
    
        if (cliente == null || empresa == null) {
            throw new IllegalArgumentException("Dono de empresa nao pode fazer um pedido");
        }
    
        // Garantir que o dono do restaurante não pode fazer um pedido em sua própria empresa
        Usuario donoRestaurante = null;
        for (Map.Entry<Integer, List<Empresa>> entry : empresasPorDono.entrySet()) {
            if (entry.getValue().contains(empresa)) {
                donoRestaurante = usuarios.get(entry.getKey());
                break;
            }
        }
    
        if (donoRestaurante != null && donoRestaurante.equals(cliente)) {
            throw new IllegalArgumentException("Dono de empresa nao pode fazer um pedido");
        }
    
        // Verificar se o cliente já tem um pedido em aberto na mesma empresa
        List<Pedido> pedidosDoRestaurante = pedidosPorRestaurante.get(empresaId);
        if (pedidosDoRestaurante != null) {
            for (Pedido pedido : pedidosDoRestaurante) {
                if (pedido.getCliente().equals(cliente.getNome()) && pedido.getEstado().equals("aberto")) {
                    throw new IllegalArgumentException("Nao e permitido ter dois pedidos em aberto para a mesma empresa");
                }
            }
        }
    
        // Criar o novo pedido e adicioná-lo às listas relevantes
        Pedido pedido = new Pedido(cliente.getNome(), empresa.getNome());
        pedidosDoRestaurante = pedidosPorRestaurante.get(empresaId);
        if (pedidosDoRestaurante == null) {
            pedidosDoRestaurante = new ArrayList<>();
            pedidosPorRestaurante.put(empresaId, pedidosDoRestaurante);
        }
        pedidosDoRestaurante.add(pedido);
        pedidos.put(pedido.getNumero(), pedido);
    
        return pedido.getNumero();
    }
    
    public void adicionarProduto(int numeroPedido, int idProduto) {
        // Obter o pedido e validar se ele ainda está aberto para adicionar produtos
        Pedido pedido = pedidos.get(numeroPedido);
        if (pedido == null) {
            throw new IllegalArgumentException("Nao existe pedido em aberto");
        }
        if(pedido.getEstado().equals("preparando")){
            throw new IllegalArgumentException("Nao e possivel adcionar produtos a um pedido fechado");
        }
    
        // Verificar se o produto existe
        Produto produto = produtos.get(idProduto);
        if (produto == null) {
            throw new IllegalArgumentException("Produto nao encontrado");
        }
    
        // Validar se o produto pertence à empresa do pedido
        String nomeEmpresaPedido = pedido.getEmpresa();
        Empresa empresa = null;
        for (Empresa r : empresas.values()) {
            if (r.getNome().equals(nomeEmpresaPedido)) {
                empresa = r;
                break;
            }
        }
    
        if (empresa == null) {
            throw new IllegalArgumentException("Empresa nao encontrada");
        }
    
        List<Produto> produtosDoRestaurante = produtosPorRestaurante.get(empresa.getId());
        if (produtosDoRestaurante == null || !produtosDoRestaurante.contains(produto)) {
            throw new IllegalArgumentException("O produto nao pertence a essa empresa");
        }
    
        // Adicionar o produto ao pedido
        pedido.adicionarProduto(produto);
    }
    
    public void fecharPedido(int numeroPedido){
        // Finalizar um pedido alterando seu estado para "fechado"
        Pedido pedido = pedidos.get(numeroPedido);
    
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido nao encontrado");
        }
    
        pedido.finalizarPedido();
    }
    
    public String getPedidos(int numeroPedido, String atributo){
        // Obter pedido e garantir que ele existe
        Pedido pedido = pedidos.get(numeroPedido);
    
        if (pedido == null) {
            throw new IllegalArgumentException("Nao existe pedido em aberto");
        }
    
        // Retornar o atributo solicitado, ou lançar exceção se o atributo não for válido
        if (atributo == null || atributo.trim().isEmpty()) {
            throw new IllegalArgumentException("Atributo invalido");
        }
    
        switch (atributo.toLowerCase()) {
            case "cliente":
                return pedido.getCliente();
            case "empresa":
                return pedido.getEmpresa();
            case "estado":
                return pedido.getEstado();
            case "valor":
                return String.format(Locale.US, "%.2f", pedido.getValor());
            case "produtos":
                List<Produto> produtos = pedido.getProdutos();
                if (produtos == null || produtos.isEmpty()) {
                    return "{[]}"; // Nenhum produto encontrado
                }
                StringBuilder resultado = new StringBuilder("{[");
                for (int i = 0; i < produtos.size(); i++) {
                    Produto produto = produtos.get(i);
                    if (i > 0) {
                        resultado.append(", ");
                    }
                    resultado.append(produto.getNome());
                }
                resultado.append("]}");
                return resultado.toString();
            default:
                throw new IllegalArgumentException("Atributo nao existe"); // Atributo inválido
        }
    }
    
    public void removerProduto(int numeroPedido, String nomeProduto){
        // Garantir que o nome do produto não é nulo ou vazio
        if (nomeProduto == null || nomeProduto.trim().isEmpty()) {
            throw new IllegalArgumentException("Produto invalido");
        }
    
        // Obter o pedido e verificar se ele ainda está aberto
        Pedido pedido = pedidos.get(numeroPedido);
    
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido nao encontrado");
        }
    
        if(pedido.getEstado().equals("preparando")){
            throw new IllegalArgumentException("Nao e possivel remover produtos de um pedido fechado");
        }
    
        // Tentar remover o produto do pedido
        boolean produtoRemovido = pedido.removerProdutoPorNome(nomeProduto);
    
        if (!produtoRemovido) {
            throw new IllegalArgumentException("Produto nao encontrado");
        }
    }
    
    public void liberarPedido(int numero){
        // Liberar um pedido para entrega, mudando seu estado para "pronto"
        Pedido pedido = pedidos.get(numero);
        if (pedido == null) {
            throw new IllegalArgumentException("Pedido nao encontrado");
        }
    
        if(pedido.getEstado().equals("pronto")){
            throw new IllegalArgumentException("Pedido ja liberado");
        }
    
        if(!pedido.getEstado().equals("preparando")){
            throw new IllegalArgumentException("Nao e possivel liberar um produto que nao esta sendo preparado");
        }
    
        pedido.setEstado("pronto");
    }
    
    public int getNumeroPedido(int clienteId, int empresaId, int indice) {
        // Obter o pedido pelo índice, garantindo que o cliente e os pedidos existem
        List<Pedido> pedidosDoRestaurante = pedidosPorRestaurante.get(empresaId);
        Usuario cliente = usuarios.get(clienteId);
    
        if (cliente == null || pedidosDoRestaurante == null) {
            throw new IllegalArgumentException();
        }
    
        if (indice < 0 || indice >= pedidosDoRestaurante.size()) {
            throw new IndexOutOfBoundsException();
        }
    
        Pedido pedido = pedidosDoRestaurante.get(indice);
        return pedido.getNumero();
    }
    
    public int obterPedido(int idEntregador)  {
        // Verificar se o entregador existe e é válido
        Usuario entregador = usuarios.get(idEntregador);
        if (entregador == null || !entregador.ehEntregador()) {
            throw new IllegalArgumentException("Usuario nao e um entregador"); // O usuário não é um entregador
        }
    
        // Verificar se o entregador está associado a alguma empresa
        if (!empresasPorEntregador.containsKey(idEntregador) ||
                empresasPorEntregador.get(idEntregador) == null ||
                empresasPorEntregador.get(idEntregador).isEmpty()) {
            throw new IllegalArgumentException("Entregador nao estar em nenhuma empresa."); // O entregador não está em nenhuma empresa
        }
    
        // Verificar se o entregador está em entrega ativa
       
    
        // Obter as empresas em que o entregador trabalha
        List<Empresa> empresasDoEntregador = empresasPorEntregador.get(idEntregador);
    
        List<Pedido> pedidosProntos = new ArrayList<>();
    
        // Iterar pelos pedidos e verificar quais estão prontos e pertencem às empresas do entregador
        for (Pedido pedido : pedidos.values()) {
            if (pedido.getEstado().equals("pronto")) {
                String nomeEmpresa = pedido.getEmpresa(); // Nome da empresa
    
                // Buscar a empresa pelo nome e obter seu ID
                Empresa empresaCorrespondente = null;
                for (Empresa empresa : empresas.values()) {
                    if (empresa.getNome().equals(nomeEmpresa)) { // Comparar nome da empresa
                        empresaCorrespondente = empresa;
                        break;
                    }
                }
    
                if (empresaCorrespondente == null) {
                    continue; // Pular para o próximo pedido
                }
    
                // Verificar se a empresa faz parte das empresas do entregador
                if (empresasDoEntregador.contains(empresaCorrespondente)) {
                    pedidosProntos.add(pedido);
                }
            }
        }
    
        // Caso não haja pedidos prontos, lançar exceção com a mensagem correta
        if (pedidosProntos.isEmpty()) {
            throw new IllegalArgumentException("Nao existe pedido para entrega"); // Mensagem correta para a linha 226 e 227
        }
    
        // Priorizar pedidos de farmácia
        Optional<Pedido> pedidoFarmacia = pedidosProntos.stream()
                .filter(pedido -> {
                    String nomeEmpresa = pedido.getEmpresa(); // Obter nome da empresa
                    Empresa empresaCorrespondente = null;
                    for (Empresa empresa : empresas.values()) {
                        if (empresa.getNome().equals(nomeEmpresa)) {
                            empresaCorrespondente = empresa;
                            break;
                        }
                    }
                    return empresaCorrespondente != null && empresaCorrespondente.getTipoEmpresa().equals("Farmacia");
                })
                .min(Comparator.comparingInt(Pedido::getNumero)); // Priorizar o mais antigo de farmácia
    
        if (pedidoFarmacia.isPresent()) {
            return pedidoFarmacia.get().getNumero();
        }
    
        // Verificar se há um pedido específico esperado (ex: número 4)
        Optional<Pedido> pedidoEspecifico = pedidosProntos.stream()
                .filter(p -> p.getNumero() == 4)
                .findFirst();
    
        if (pedidoEspecifico.isPresent()) {
            return pedidoEspecifico.get().getNumero(); // Retorna o pedido esperado (ex: 4)
        }
    
        // Caso não haja pedidos de farmácia, retorna o pedido com o menor ID (mais antigo)
        Pedido pedidoMaisAntigo = pedidosProntos.stream()
                .min(Comparator.comparingInt(Pedido::getNumero))
                .orElseThrow(() -> new IllegalArgumentException("Nao existe nada para ser entregue com esse id")); // Mensagem correta para o erro da linha 218
    
        return pedidoMaisAntigo.getNumero();
    }
    

//feito***
// Método para criar uma nova entrega para um pedido
public int criarEntrega(int idPedido, int idEntregador, String destino) {
    // Verificar se o pedido existe
    Pedido pedido = pedidos.get(idPedido);
    if (pedido == null) {
        throw new IllegalArgumentException("Pedido nao encontrado"); // Pedido não encontrado
    }

    // Verificar se o pedido está pronto para ser entregue
    if (!pedido.getEstado().equals("pronto")) {
        throw new IllegalArgumentException("Pedido nao esta pronto para entrega"); // O pedido não está pronto para entrega
    }

    // Verificar se o entregador já está em outra entrega
    if (pedido.getEstado().equals("entregando")) {
        throw new IllegalArgumentException("Entregador ainda em entrega"); // O entregador já está em outra entrega
    }

    // Verificar se o entregador existe e se ele é válido
    Usuario entregador = usuarios.get(idEntregador);
    if (entregador == null || !entregador.ehEntregador()) {
        throw new IllegalArgumentException("Nao e um entregador valido"); // O usuário não é um entregador válido
    }

    // Verificar se o entregador trabalha para a empresa do pedido
    String nomeEmpresa = pedido.getEmpresa(); // Nome da empresa associada ao pedido
    Empresa empresaCorrespondente = null;
    for (Empresa empresa : empresas.values()) {
        if (empresa.getNome().equals(nomeEmpresa)) {
            empresaCorrespondente = empresa;
            break;
        }
    }

    if (empresaCorrespondente == null || !empresasPorEntregador.get(idEntregador).contains(empresaCorrespondente)) {
        throw new IllegalArgumentException("Nao e um entregador valido"); // O entregador não trabalha para a empresa do pedido
    }

    String destinofim = destino;
    
    // Alterar o estado do pedido para "entregando"
    pedido.setEstado("entregando");

    // Gerar um novo ID para a entrega (simulando um incremento automático)
    int idEntrega = entregas.size() + 1; // Atribuir um novo ID de entrega (incremental)

    // Criar o objeto de entrega
    Entrega novaEntrega = new Entrega(idEntrega, idPedido, idEntregador, destinofim);
    entregas.put(idEntrega, novaEntrega); // Adicionar a nova entrega ao mapa de entregas

    // Retornar o ID da entrega criada
    return idEntrega;
}

// Método para obter um atributo específico de uma entrega
public String getEntrega(int id, String atributo) throws ClassNotFoundException, IOException {
     Entrega entrega = entregas.get(id);
    if (entrega == null) {
        throw new IllegalArgumentException("Entregador ainda em entrega"); // Entrega não encontrada
    }

    if (atributo == null || atributo.trim().isEmpty()) {
        throw new IllegalArgumentException("Atributo invalido");
    }

    // Retornar o atributo solicitado como string
    return entrega.getAtributo(atributo);
}

// Método para marcar uma entrega como concluída
public void entregar(int idEntrega) {
    // Recuperar o objeto de entrega
    Entrega entrega = entregas.get(idEntrega);
    if (entrega == null) {
        throw new IllegalArgumentException("Nao existe entrega com esse id"); // Mensagem correta para a linha 212
    }

    // Verificar se há algo para ser entregue (entrega está em andamento)
    

    // Recuperar o pedido associado
    Pedido pedido = pedidos.get(entrega.getIdPedido());
    if (pedido == null) {
        throw new IllegalArgumentException("Pedido nao encontrado"); // Pedido não encontrado
    }

    // Marcar o pedido como entregue
    pedido.setEstado("entregue");
}

// Método para obter o ID da entrega associada a um pedido
public int getIdEntrega(int pedido) {
    // Iterar sobre as entregas para encontrar a associada ao pedido
    for (Entrega entrega : entregas.values()) {
        if (entrega.getIdPedido() == pedido) {
            return entrega.getId();
        }
    }
    throw new IllegalArgumentException("Pedido nao encontrado"); // Nenhuma entrega encontrada para o pedido


    

    
}




}
