import { useQuery } from '@tanstack/react-query'
import React from 'react'
import { QUERY_KEY } from '../key'
import { fetchEmployees } from '../api/employees'

const useEmployeesQuery = () => {
    return useQuery({
        queryKey: [QUERY_KEY.EMPLOYEES],
        queryFn: fetchEmployees
    })
}

export default useEmployeesQuery
