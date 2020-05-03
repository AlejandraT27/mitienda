export interface IShoppingCar {
  id?: number;
  numberProduct?: string;
  product?: string;
  description?: string;
  quantity?: number;
  totalPurchase?: number;
  userId?: number;
}

export class ShoppingCar implements IShoppingCar {
  constructor(
    public id?: number,
    public numberProduct?: string,
    public product?: string,
    public description?: string,
    public quantity?: number,
    public totalPurchase?: number,
    public userId?: number
  ) {}
}
