import dayjs from 'dayjs';
import 'dayjs/locale/pt-br';

dayjs.locale('pt-br');

export const formatCurrency = (value: number | undefined | null) => {
  if (value === undefined || value === null) return 'R$ 0,00';
  return new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL',
  }).format(value);
};

export const formatDate = (dateString: string | undefined | null) => {
  if (!dateString) return '-';
  return dayjs(dateString).format('DD/MM/YYYY');
};

export const formatMonthYear = (month: number, year: number) => {
  return dayjs(`${year}-${month}-01`).format('MMMM [de] YYYY');
};
