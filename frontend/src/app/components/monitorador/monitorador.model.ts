export interface Monitorador {
    id: number | null
    tipo: string | null
    nome: string | null
    razaoSocial: string| null
    cpf: string | null
    cnpj: string | null
    rg: string | null
    inscricaoEstadual: string | null
    dataNascimento: Date | null
    email: String | null
    ativo: boolean | null
    enderecos: Enderecos[] | null
}

export interface Enderecos {
    id: number | null
    endereco: string | null
    numero: string | null
    cep: string | null
    bairro: string | null
    telefone: string | null
    cidade: string | null
    estado: string | null
    principal: boolean | null
}

export interface CEPModel {
    cep: string
    logradouro: string
    complemento: string
    bairro: string
    localidade: string
    uf: string
    ibge: string
    gia: string
    ddd: string
    siafi: string
}

export interface PaginatedResponse {
    content: Monitorador[]
    totalElements: number
}
