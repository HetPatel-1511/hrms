import { useQuery } from '@tanstack/react-query'
import React from 'react'
import { QUERY_KEY } from '../key'
import { fetchEmployees } from '../api/employees'

const useEmployeesQuery = (search?: string) => {
    return useQuery({
        queryKey: [QUERY_KEY.EMPLOYEES, search],
        queryFn: () => fetchEmployees(search),
        staleTime: 5000
    })
}

export default useEmployeesQuery
