import { QueryClient, useMutation, useQueryClient } from '@tanstack/react-query'
import { login } from '../api/login'
import { QUERY_KEY } from '../key';

const useLoginMutation = () => {
    return useMutation({
        mutationFn: login
    })
}

export default useLoginMutation
