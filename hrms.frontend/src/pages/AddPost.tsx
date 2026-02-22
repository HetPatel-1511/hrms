import { useEffect } from 'react'
import { useForm } from 'react-hook-form'
import { useNavigate } from 'react-router'
import Button from '../components/Button'
import FormInput from '../components/TextInput'
import useAddPostMutation from '../query/queryHooks/useAddPostMutation'
import { toast } from 'react-toastify'

const AddPost = () => {
    const navigate = useNavigate()
    const { register, handleSubmit, formState: { errors } } = useForm()
    const addPost = useAddPostMutation()

    const onSubmit = (data: any) => {
        const payload: any = new FormData()
        payload.append('title', data.title)
        if (data.description) payload.append('description', data.description)
        if (data.tags) {
            const tags = (data.tags || '').split(',').map((t: string) => t.trim()).filter((t: string) => t)
            tags.forEach((tag: string) => payload.append('tags', tag))
        }
        if (data.media && data.media.length > 0) {
            payload.append('media', data.media[0])
        }

        addPost.mutate(payload)
    }

    useEffect(() => {
        if (addPost.isSuccess) {
            toast.success('Post created successfully')
            navigate('/post')
        }
    }, [addPost.isSuccess])

    return (
        <div>
            <h1 className='text-2xl font-bold mb-4'>Add Post</h1>
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
                <div className="mb-5">
                    <FormInput
                        type="file"
                        label="Media"
                        id="media"
                        register={register}
                        errors={errors}
                        accept="image/*"
                        validation={{
                            required: 'Media is mandatory',
                            validate: (files: any) => (
                                files && files[0] && files[0].type && files[0].type.startsWith('image')
                            ) || 'Media must be an image'
                        }}
                    />
                </div>

                <Button onClick={handleSubmit(onSubmit)} disabled={addPost.isPending}>
                    {addPost.isPending ? 'Creating...' : 'Create Post'}
                </Button>
            </form>
        </div>
    )
}

export default AddPost
