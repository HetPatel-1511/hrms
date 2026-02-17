import { useMutation } from '@tanstack/react-query'
import { referFriend } from '../api/jobOpening';

const useReferFriendMutation = () => {
    return useMutation({
        mutationFn: referFriend,
    })
}

export default useReferFriendMutation
