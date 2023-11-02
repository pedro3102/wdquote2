export interface IStockPosition {
  id?: number
  name?: string
  description?: string
}

export class StockPosition implements IStockPosition {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
  ) {
  }
}
