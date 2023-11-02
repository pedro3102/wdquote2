import { ILanguage } from '@/views/home/mod/inventory/language/language-model'
import { ICustomer } from '@/views/home/mod/inventory/customer/customer-model'

export interface ICustomerContact {
  id?: number
  code?: string
  name?: string
  phone?: string
  email?: string
  salespersonCode?: string
  isDefaultContact?: boolean
  customer?: ICustomer
  language?: ILanguage
}

export class CustomerContact implements ICustomerContact {
  constructor(
    public id?: number,
    public code?: string,
    public name?: string,
    public phone?: string,
    public email?: string,
    public salespersonCode?: string,
    public isDefaultContact = false,
    public customer?: ICustomer,
    public language?: ILanguage
  ) {}
}
