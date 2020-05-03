import { ICustomer } from 'app/shared/model/customer.model';
import { IProductCategory } from 'app/shared/model/product-category.model';
import { Size } from 'app/shared/model/enumerations/size.model';
import { Color } from 'app/shared/model/enumerations/color.model';

export interface IProduct {
  id?: number;
  name?: string;
  description?: string;
  purchasePrice?: number;
  salePrice?: number;
  size?: Size;
  color?: Color;
  stock?: string;
  imageContentType?: string;
  image?: any;
  userId?: number;
  productCategoys?: ICustomer[];
  productCategories?: IProductCategory[];
}

export class Product implements IProduct {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public purchasePrice?: number,
    public salePrice?: number,
    public size?: Size,
    public color?: Color,
    public stock?: string,
    public imageContentType?: string,
    public image?: any,
    public userId?: number,
    public productCategoys?: ICustomer[],
    public productCategories?: IProductCategory[]
  ) {}
}
