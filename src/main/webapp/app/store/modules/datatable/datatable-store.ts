import { defineStore } from 'pinia'

export interface IDataTableState {
  isEditing?: boolean
}

export const initialDataTableState: IDataTableState = {
  isEditing: false,
}

export const useDataTableStore = defineStore({
  id: 'datatable',
  state: () => initialDataTableState,
  getters: {
    getIsEditing: (state): object => state.isEditing,
  },
  actions: {
    setIsEditing(isEditing: boolean): void {
      this.isEditing = isEditing
    },
    resetDatatableState(): void {
      this.isEditing = false
    },
  },
})
