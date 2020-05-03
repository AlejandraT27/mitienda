export interface IPayment {
  id?: number;
  firstName?: string;
  lastName?: string;
  wayToPay?: string;
  userId?: number;
}

export class Payment implements IPayment {
  constructor(public id?: number, public firstName?: string, public lastName?: string, public wayToPay?: string, public userId?: number) {}
}
