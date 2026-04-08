import { api } from './axios';
import { TransactionResponse, CreateTransactionCommand, UpdateTransactionCommand } from '../types';

export const transactionApi = {
  findAllByUserId: async (userId: string): Promise<TransactionResponse[]> => {
    const { data } = await api.get<TransactionResponse[]>('/api/v1/transactions', { params: { userId } });
    return data;
  },
  create: async (command: CreateTransactionCommand): Promise<TransactionResponse> => {
    const { data } = await api.post<TransactionResponse>('/api/v1/transactions', command);
    return data;
  },
  update: async (id: string, command: UpdateTransactionCommand): Promise<TransactionResponse> => {
    const { data } = await api.put<TransactionResponse>(`/api/v1/transactions/${id}`, command);
    return data;
  },
  delete: async (id: string): Promise<void> => {
    await api.delete(`/api/v1/transactions/${id}`);
  }
};
