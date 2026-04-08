import { api } from './axios';
import { AuthResponse, LoginCommand, RegisterUserCommand } from '../types';

export const authApi = {
  login: async (credentials: LoginCommand): Promise<AuthResponse> => {
    const { data } = await api.post<AuthResponse>('/api/v1/auth/login', credentials);
    return data;
  },
  
  register: async (userData: RegisterUserCommand): Promise<AuthResponse> => {
    const { data } = await api.post<AuthResponse>('/api/v1/auth/register', userData);
    return data;
  },
};
