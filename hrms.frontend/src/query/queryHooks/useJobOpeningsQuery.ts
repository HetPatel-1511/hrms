import { useQuery } from '@tanstack/react-query'
import { QUERY_KEY } from '../key'
import { fetchJobOpenings } from '../api/jobOpening'

const useJobOpeningsQuery = () => {
    return useQuery({
        queryKey: [QUERY_KEY.JOB_OPENINGS],
        queryFn: fetchJobOpenings
    })
}

export default useJobOpeningsQuery
