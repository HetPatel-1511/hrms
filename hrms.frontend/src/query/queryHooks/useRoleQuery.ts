import { useQuery } from '@tanstack/react-query'
import { QUERY_KEY } from '../key'
import { fetchRoles } from '../api/employees'

const useRoleQuery = () => {
    return useQuery({
        queryKey: [QUERY_KEY.ROLES],
        queryFn: () => fetchRoles()
    })
}

export default useRoleQuery
