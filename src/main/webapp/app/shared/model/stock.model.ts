export interface IStock {
  id?: number;
  numberProduct?: string;
  nameProduct?: string;
  description?: string;
  inventory?: string;
}

export class Stock implements IStock {
  constructor(
    public id?: number,
    public numberProduct?: string,
    public nameProduct?: string,
    public description?: string,
    public inventory?: string
  ) {}
}
