import { useQuery } from '@tanstack/react-query'
import React from 'react'
import { QUERY_KEY } from '../key'
import { fetchEmployees } from '../api/employees'
import { fetchSingleConfigurations } from '../api/configurations'

const useSingleConfigurationQuery = (configKey: any) => {
    return useQuery({
        queryKey: [QUERY_KEY.CONFIGURATIONS, configKey],
        queryFn: () => fetchSingleConfigurations(configKey)
    })
}

export default useSingleConfigurationQuery
