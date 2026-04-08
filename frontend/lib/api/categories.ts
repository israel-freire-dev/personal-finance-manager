import { api } from './axios';
import { CategoryResponse, CreateCategoryCommand, UpdateCategoryCommand } from '../types';

export const categoryApi = {
  findAllByUserId: async (userId: string): Promise<CategoryResponse[]> => {
    const { data } = await api.get<CategoryResponse[]>('/api/v1/categories', { params: { userId } });
    return data;
  },
  create: async (command: CreateCategoryCommand): Promise<CategoryResponse> => {
    const { data } = await api.post<CategoryResponse>('/api/v1/categories', command);
    return data;
  },
  update: async (id: string, command: UpdateCategoryCommand): Promise<CategoryResponse> => {
    const { data } = await api.put<CategoryResponse>(`/api/v1/categories/${id}`, command);
    return data;
  },
  delete: async (id: string): Promise<void> => {
    await api.delete(`/api/v1/categories/${id}`);
  }
};
