import { useQuery } from '@tanstack/react-query'
import { QUERY_KEY } from '../key'
import { fetchSingleEmployee } from '../api/employees'

const useSingleEmployeeQuery = (id: any) => {
    return useQuery({
        queryKey: [QUERY_KEY.SINGLE_EMPLOYEES, id],
        queryFn: () => fetchSingleEmployee(id)
    })
}

export default useSingleEmployeeQuery
