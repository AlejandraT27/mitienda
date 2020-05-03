export interface ISeller {
  id?: number;
  firstName?: string;
  userId?: number;
}

export class Seller implements ISeller {
  constructor(public id?: number, public firstName?: string, public userId?: number) {}
}
