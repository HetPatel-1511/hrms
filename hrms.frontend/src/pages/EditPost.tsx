import { useEffect } from 'react'
import { useForm } from 'react-hook-form'
import { useNavigate, useParams } from 'react-router'
import Button from '../components/Button'
import FormInput from '../components/TextInput'
import useSinglePostQuery from '../query/queryHooks/useSinglePostQuery'
import useUpdatePostMutation from '../query/queryHooks/useUpdatePostMutation'
import Loading from '../components/Loading'
import ServerError from '../components/ServerError'
import { toast } from 'react-toastify'

const EditPost = () => {
    const { postId } = useParams()
    const navigate = useNavigate()
    const { register, handleSubmit, formState: { errors }, setValue } = useForm()

    const { data, isLoading, isError } = useSinglePostQuery(postId)
    const updatePost = useUpdatePostMutation()

    useEffect(() => {
        if (data?.data) {
            const post = data.data
            setValue('title', post.title)
            setValue('description', post.description)
            setValue('tags', post.tags ? post.tags.map((tag: any) => tag.tag).join(', ') : '')
        }
    }, [data?.data])

    useEffect(() => {
        if (updatePost.isSuccess) {
            toast.success('Post updated successfully')
            navigate('/post')
        }
    }, [updatePost.isSuccess])

    const onSubmit = (formData: any) => {
        const payload: any = {
            title: formData.title,
            description: formData.description,
            tags: formData.tags ? formData.tags.split(',').map((t: string) => t.trim()).filter((t: string) => t) : []
        }
        updatePost.mutate({ postId, data: payload })
    }

    if (isLoading) return <Loading />
    if (isError) return <ServerError />

    return (
        <div>
            <h1 className='text-2xl font-bold mb-4'>Edit Post</h1>
            <form>
                <div className="mb-5">
                    <FormInput
                        label="Title"
                        id="title"
                        placeholder="Post title"
                        register={register}
                        errors={errors}
                        validation={{ required: 'Title is mandatory' }}
                    />
                </div>
                <div className="mb-5">
                    <FormInput
                        label="Description"
                        id="description"
                        placeholder="Post description"
                        register={register}
                        errors={errors}
                    />
                </div>
                <div className="mb-5">
                    <FormInput
                        label="Tags (comma separated)"
                        id="tags"
                        placeholder="tag1, tag2"
                        register={register}
                        errors={errors}
                    />
                </div>

                <Button onClick={handleSubmit(onSubmit)} disabled={updatePost.isPending}>
                    {updatePost.isPending ? 'Updating...' : 'Update Post'}
                </Button>
            </form>
        </div>
    )
}

export default EditPost
