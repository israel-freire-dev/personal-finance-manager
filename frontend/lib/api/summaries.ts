import { api } from './axios';
import { MonthlySummaryResponse } from '../types';

export const summaryApi = {
  getSummary: async (userId: string, month: number, year: number): Promise<MonthlySummaryResponse> => {
    const { data } = await api.get<MonthlySummaryResponse>('/api/v1/summaries', {
      params: { userId, month, year }
    });
    return data;
  }
};
