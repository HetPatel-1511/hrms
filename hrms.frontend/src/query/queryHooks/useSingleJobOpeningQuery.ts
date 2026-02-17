import { useQuery } from '@tanstack/react-query'
import { QUERY_KEY } from '../key'
import { fetchSingleJobOpening } from '../api/jobOpening'

const useSingleJobOpeningQuery = (id: any) => {
    return useQuery({
        queryKey: [QUERY_KEY.JOB_OPENINGS, id],
        queryFn: () => fetchSingleJobOpening(id)
    })
}

export default useSingleJobOpeningQuery
