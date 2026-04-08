export type TransactionStatus = 'PENDING' | 'PAID' | 'REFUNDED' | 'INVESTED' | 'CANCELLED';
export type TransactionType = 'INCOME' | 'EXPENSE';
export type RecurringFrequency = 'MONTHLY' | 'WEEKLY' | 'ANNUAL';

export interface User {
  id: string;
  name: string;
  email: string;
}

export interface AuthResponse {
  token: string;
  userId: string;
  name: string;
  email: string;
}

export interface TransactionResponse {
  id: string;
  userId: string;
  categoryId: string;
  description: string;
  amount: number;
  date: string;
  status: TransactionStatus;
  type: TransactionType;
  recurringTemplateId?: string;
  createdAt: string;
  updatedAt: string;
}

export interface CategoryResponse {
  id: string;
  userId: string;
  parentId?: string;
  name: string;
  type: TransactionType;
  color?: string;
  icon?: string;
  createdAt: string;
  updatedAt: string;
}

export interface MonthlySummaryResponse {
  id: string;
  userId: string;
  month: number;
  year: number;
  openingBalance: number;
  totalIncome: number;
  totalExpenses: number;
  closingBalance: number;
  totalIncomePending: number;
  totalExpensesPending: number;
  projectedBalance: number;
  isClosed: boolean;
}

export interface RecurringTemplateResponse {
  id: string;
  userId: string;
  categoryId: string;
  description: string;
  amount: number;
  frequency: RecurringFrequency;
  startDate: string;
  endDate?: string;
  totalInstallments?: number;
  lastGeneratedDate?: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreateTransactionCommand {
  userId: string;
  categoryId: string;
  description: string;
  amount: number;
  date: string;
  status: TransactionStatus;
  type: TransactionType;
  recurringTemplateId?: string;
}

export interface UpdateTransactionCommand {
  description: string;
  amount: number;
  date: string;
  status: TransactionStatus;
  categoryId: string;
}

export interface CreateCategoryCommand {
  userId: string;
  parentId?: string;
  name: string;
  type: TransactionType;
  color?: string;
  icon?: string;
}

export interface UpdateCategoryCommand {
  parentId?: string;
  name: string;
  color?: string;
  icon?: string;
}

export interface CreateRecurringTemplateCommand {
  userId: string;
  categoryId: string;
  description: string;
  amount: number;
  frequency: RecurringFrequency;
  startDate: string;
  endDate?: string;
  totalInstallments?: number;
}

export interface LoginCommand {
  email: string;
  password?: string;
}

export interface RegisterUserCommand {
  name: string;
  email: string;
  password?: string;
}

