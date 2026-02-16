import { useQuery } from '@tanstack/react-query'
import React from 'react'
import { QUERY_KEY } from '../key'
import { fetchEmployees } from '../api/employees'
import { fetchConfigurations } from '../api/configurations'

const useConfigurationQuery = () => {
    return useQuery({
        queryKey: [QUERY_KEY.CONFIGURATIONS],
        queryFn: fetchConfigurations
    })
}

export default useConfigurationQuery
