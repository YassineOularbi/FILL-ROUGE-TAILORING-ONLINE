export interface PaginationState {
    page: number;
    size: number;
    sortField: string;
    sortDirection: string;
}

export const initialPaginationState: PaginationState = {
    page: 1,
    size: 10,
    sortField: 'name',
    sortDirection: 'asc'
};