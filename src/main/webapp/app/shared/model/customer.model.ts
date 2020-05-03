export interface ICustomer {
  id?: number;
  firstName?: string;
  lastName?: string;
  idNumber?: string;
  email?: string;
  phone?: string;
  addresLine?: string;
  userId?: number;
  productoNombre?: string;
  productoId?: number;
}

export class Customer implements ICustomer {
  constructor(
    public id?: number,
    public firstName?: string,
    public lastName?: string,
    public idNumber?: string,
    public email?: string,
    public phone?: string,
    public addresLine?: string,
    public userId?: number,
    public productoNombre?: string,
    public productoId?: number
  ) {}
}
