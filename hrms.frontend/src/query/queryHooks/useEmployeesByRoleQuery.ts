import { useQuery } from '@tanstack/react-query'
import { QUERY_KEY } from '../key'
import { fetchEmployeesByRole } from '../api/employees'

const useEmployeesByRoleQuery = (roleName: string) => {
    return useQuery({
        queryKey: [QUERY_KEY.EMPLOYEES, roleName],
        queryFn: () => fetchEmployeesByRole(roleName)
    })
}

export default useEmployeesByRoleQuery
