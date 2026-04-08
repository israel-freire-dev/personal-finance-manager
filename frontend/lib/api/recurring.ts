import { api } from './axios';
import { RecurringTemplateResponse, CreateRecurringTemplateCommand } from '../types';

export const recurringApi = {
  findAllByUserId: async (userId: string): Promise<RecurringTemplateResponse[]> => {
    const { data } = await api.get<RecurringTemplateResponse[]>('/api/v1/recurring-templates', { params: { userId } });
    return data;
  },
  create: async (command: CreateRecurringTemplateCommand): Promise<RecurringTemplateResponse> => {
    const { data } = await api.post<RecurringTemplateResponse>('/api/v1/recurring-templates', command);
    return data;
  },
  delete: async (id: string): Promise<void> => {
    await api.delete(`/api/v1/recurring-templates/${id}`);
  }
};
