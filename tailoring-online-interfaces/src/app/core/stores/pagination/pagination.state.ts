export interface PaginationState {
    page: number;
    size: number;
    sortField: string;
    sortDirection: string;
    totalRecords: number;
}

export const initialPaginationState: PaginationState = {
    page: 0,
    size: 9,
    sortField: 'name',
    sortDirection: 'asc',
    totalRecords: 0
};