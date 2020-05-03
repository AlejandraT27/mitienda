export interface IProductCategory {
  id?: number;
  name?: string;
  description?: string;
  productoNombre?: string;
  productoId?: number;
}

export class ProductCategory implements IProductCategory {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public productoNombre?: string,
    public productoId?: number
  ) {}
}
