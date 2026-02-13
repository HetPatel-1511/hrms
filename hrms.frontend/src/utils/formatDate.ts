export default function formatDate(date: any, format: any) {
  return new Date(date).toLocaleDateString('en-US', format)
}
