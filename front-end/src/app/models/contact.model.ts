export interface Contact {
  id: number;
  type: 'PF' | 'PJ';
  // Common fields
  email: string;
  phoneNumber: string;
  description: string;
  addresses: Address[];
  // PF specific fields
  cpf?: string;
  firstName?: string;
  surName?: string;
  // PJ specific fields
  cnpj?: string;
  cnpjName?: string;
}

export interface Address {
  id: number;
  cep: string;
  contryState: string;
  stateCity: string;
  cityNeighborhood: string;
  address: string;
  addressNumber: string;
} 